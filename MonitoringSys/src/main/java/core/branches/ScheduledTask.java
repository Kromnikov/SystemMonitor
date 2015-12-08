package core.branches;

import core.SpringService;
import core.agents.SSHAgent;
import core.configurations.SSHConfiguration;
import core.hibernate.services.HostService;
import core.interfaces.db.IMetricStorage;
import core.models.Metric;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class ScheduledTask extends TimerTask {
    private static SSHAgent sshAgent;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Logger logger = Logger.getLogger(ScheduledTask.class);
    private static boolean hostState;

    @Override
    public void run() {
        try {
            IMetricStorage metricStorage = SpringService.getMetricStorage();
            HostService hosts = SpringService.getHosts();
            SSHAgent sshAgent;
            for (SSHConfiguration host : hosts.getAll()) {
                sshAgent = new SSHAgent(host);
                hostState=metricStorage.getState(host.getId());
                if (sshAgent.connect()) {
                    if(!hostState) {
                        metricStorage.setTrueStateHost(dateFormat.format(new Date()), host.getId());
                    }
                    for (Metric metric : metricStorage.getMetricsByHostId(host.getId())) {
//                        System.out.println(host.getId() + "////" + metric.getTitle() + ":" + metric.getId() + "////" + sshAgent.getMetricValue(metric));
                        logger.info("Insert to db row: (" + host.getId() + "," + metric.getId() + "," + sshAgent.getMetricValue(metric) + "," + dateFormat.format(new Date()) + ")");
                        metricStorage.addValue(host.getId(), metric.getId(), sshAgent.getMetricValue(metric), dateFormat.format(new Date()));
                    }
                } else {
                    if(hostState) {
                        metricStorage.setFalseStateHost(dateFormat.format(new Date()), host.getId());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
