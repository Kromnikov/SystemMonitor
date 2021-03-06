package net.core.agents;


import com.jcraft.jsch.*;
import net.core.configurations.SSHConfiguration;
import net.core.models.InstanceMetric;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SSHAgent {

    private static Session session;

    private static Channel channel;

    private SSHConfiguration configuration;

    private InputStream in;

    public SSHAgent(SSHConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean connect() {
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            session = jsch.getSession(configuration.getLogin(), configuration.getHost(), configuration.getPort());
            session.setPassword(configuration.getPassword());
            session.setConfig(config);
            try {
                session.connect();
            } catch (com.jcraft.jsch.JSchException e) {
                System.out.println("err connection to host: "+this.configuration.getHost());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public double getMetricValue(InstanceMetric instanceMetric) {
        try {
            this.channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(instanceMetric.getCommand());
            channel.setInputStream(null);
//            ((ChannelExec) channel).setErrStream(System.err);
            in = channel.getInputStream();
            channel.connect();

            return getMetricValue();
        } catch (JSchException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return 0;
    }

    public void disconnect() {
        if(this.channel!=null)
        this.channel.disconnect();
        if(this.session!=null)
        this.session.disconnect();
    }

    public double getMetricValue() {
        try {
            byte[] tmp = new byte[1024];
            double result = 0;
            while (true) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) return Integer.MIN_VALUE;
                    return summRows(Arrays.copyOfRange(tmp, 0, i));
//                }
            }
        } catch (Exception ee) {
            System.out.println("???????? ?????? = null");
        }
        return 0;
    }

    private double summRows(byte[] array) throws UnsupportedEncodingException {
        List<Byte> tmp = new ArrayList<Byte>();
        StringBuilder resulttemp= new StringBuilder();
        double result = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                if (array[i] == 10) {
                    for (byte item : tmp) {
                        resulttemp.append((char) item);
                    }
                    result += Double.parseDouble(resulttemp.toString());
                    resulttemp.setLength(0);
                    tmp.clear();

                } else {
                    tmp.add(array[i]);
                }
            }
            else return result;
        }
        return result;
    }
}
