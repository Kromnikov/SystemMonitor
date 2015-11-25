package core.agents.ssh;


import com.jcraft.jsch.*;
import core.models.Metric;
import core.configurations.SSHConfiguration;

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
            session.connect();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public double getMetricValue(Metric metric) {
        try {

            this.channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(metric.getCommand());
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            channel.connect();

            return getMetricValue(in);
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void disconnect() {
        if(this.channel!=null)
        this.channel.disconnect();
        if(this.session!=null)
        this.session.disconnect();
    }

    public double getMetricValue(InputStream in) {
        try {
            byte[] tmp = new byte[1024];
            double result = 0;
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) return 0;
                    return summRows(Arrays.copyOfRange(tmp, 0, i));
                }
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
