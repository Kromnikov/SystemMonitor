package core.branches;

import core.SpringService;
import core.agents.SSHAgent;
import core.configurations.SSHConfiguration;
import core.hibernate.services.HostService;
import core.interfaces.db.IMetricStorage;
import core.models.InstanceMetric;
import core.models.TemplateMetric;
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
                    if(!hostState) {//Если хост последний раз был не доступен, то выставляем дату окончания данного статуса
                        metricStorage.setTrueStateHost(dateFormat.format(new Date()), host.getId());
                    }
                    for (InstanceMetric instanceMetric : metricStorage.getMetricsByHostId(host.getId())) {//TODO getMetricsByHostId - нету теперь
                        logger.info("Insert to db row: (" + host.getId() + "," + instanceMetric.getId() + "," + sshAgent.getMetricValue(instanceMetric) + "," + dateFormat.format(new Date()) + ")");
                        metricStorage.addValue(host.getId(), instanceMetric.getId(), sshAgent.getMetricValue(instanceMetric), dateFormat.format(new Date()));
                    }
                } else {
                    if(hostState) {//Если хост последний раз был доступен
                        metricStorage.setFalseStateHost(dateFormat.format(new Date()), host.getId());
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
