package core.branches;

import core.SpringService;
import core.agents.ssh.SSHAgent;
import core.hibernate.services.HostService;
import core.interfaces.db.IMetricStorage;
import core.models.Metric;
import core.configurations.SSHConfiguration;
import org.apache.log4j.Logger;

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
    private static final Logger logger = Logger.getLogger(ScheduledTask.class);

    @Override
    public void run() {
        try {
            IMetricStorage metricStorage =((IMetricStorage) SpringService.getContext().getBean("MetricStorage"));
            HostService hosts = ((HostService) SpringService.getContext().getBean("HostService"));
            SSHAgent sshAgent;
            for (SSHConfiguration host : hosts.getAll() ) {
                sshAgent = new SSHAgent(host);
                if (sshAgent.connect()) {
                    for (Metric metric :metricStorage.getMetricsByHostId(host.getId())) {
//                        System.out.println(host.getId() + "////" + metric.getTitle() + ":" + metric.getId() + "////" + sshAgent.getMetricValue(metric));
                        logger.info("Insert to db row: ("+host.getId()+","+ metric.getId()+","+ sshAgent.getMetricValue(metric)+","+ dateFormat.format(new Date())+")");
                        metricStorage.addValue(host.getId(), metric.getId(), sshAgent.getMetricValue(metric), dateFormat.format(new Date()));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
