package core.branches;

import core.agents.SSH.SSHAgent;
import core.agents.SSH.Metric;
import core.configurations.SSHConfiguration;

import java.sql.SQLException;
import java.util.TimerTask;

public class ScheduledTask extends TimerTask {
    private static SSHAgent sshAgent;


    @Override
    public void run() {
        try {
            for (SSHConfiguration host : SQLBranch.getHosts()) {//По хостам
                sshAgent = new SSHAgent(host);
                if (sshAgent.connect()) {//Подключение к хосту
                    for (Metric metric : SQLBranch.getMetricsByHostId(host.getId())) {//По метрикам хоста
                        SQLBranch.addValue(host.getId(), metric.getId(), sshAgent.getMetricValue(metric));
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
