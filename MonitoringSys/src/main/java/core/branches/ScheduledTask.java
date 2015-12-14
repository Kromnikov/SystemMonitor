package core.branches;

import core.SpringService;
import core.agents.SSHAgent;
import core.configurations.SSHConfiguration;
import core.hibernate.services.HostService;
import core.interfaces.db.IMetricStorage;
import core.models.InstanceMetric;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class ScheduledTask extends TimerTask {
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Logger logger = Logger.getLogger(ScheduledTask.class);

    private IMetricStorage metricStorage;

    private HostService hosts;

    @Autowired
    public ScheduledTask(IMetricStorage metricStorage,HostService hosts) {
        this.hosts=hosts;
        this.metricStorage=metricStorage;
    }

    @Scheduled(fixedDelay = 10000)
    @Override
    public void run() {
        try {
            for (SSHConfiguration host : hosts.getAll()) {
                SSHAgent sshAgent = new SSHAgent(host);
                boolean available=metricStorage.available(host.getId());
                if (sshAgent.connect()) {
                    if(!available) {//Если хост последний раз был не доступен, то выставляем дату окончания данного статуса
                        metricStorage.availableHost(dateFormat.format(new Date()), host.getId());
                    }
                    for (InstanceMetric instanceMetric : metricStorage.getInstMetrics(host.getId())) {
                        double valueMetric = sshAgent.getMetricValue(instanceMetric);
                        if(valueMetric!=(Integer.MIN_VALUE)) {
                            //logger.info("Insert to db row: (" + host.getId() + "," + instanceMetric.getId() + "," + valueMetric + "," + dateFormat.format(new Date()) + ")");
                            metricStorage.addValue(host.getId(), instanceMetric.getId(), valueMetric, dateFormat.format(new Date()));
                        }
                        else {
                            //статус метрики err

                        }
                    }
                } else {
                    if(available) {//Если хост последний раз был доступен
                        metricStorage.notAvailableHost(dateFormat.format(new Date()), host.getId());
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
