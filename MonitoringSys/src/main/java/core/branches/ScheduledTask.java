package core.branches;

import core.agents.ssh.SSHAgent;
import core.models.Metric;
import core.configurations.SSHConfiguration;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimerTask;

public class ScheduledTask extends TimerTask {
    private static SSHAgent sshAgent;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run() {
        try {
            for (SSHConfiguration host : SQLBranch.getHosts()) {//�� ������
                sshAgent = new SSHAgent(host);
                if (sshAgent.connect()) {//����������� � �����
                    for (Metric metric : SQLBranch.getMetricsByHostId(host.getId())) {//�� �������� �����
                        SQLBranch.addValue(host.getId(), metric.getId(), sshAgent.getMetricValue(metric), dateFormat.format(new Date()));
//                    System.out.println(host.getId()+"////"+metric.getTitle()+":"+metric.getId()+"////"+sshAgent.getMetricValue(metric));
                    }
                    if (sshAgent != null) {
                        sshAgent.disconnect();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
