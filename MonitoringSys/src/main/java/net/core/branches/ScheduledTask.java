package net.core.branches;

import net.core.agents.SSHAgent;
import net.core.configurations.SSHConfiguration;
import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.InstanceMetric;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

//@Service("ScheduledTask")
@Component
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

    @Scheduled(initialDelay = 1000,fixedDelay = 10000)
    @Override
    public void run() {
        System.out.println("ScheduledTask");
        try {
            for (SSHConfiguration host : hosts.getAll()) {
                SSHAgent sshAgent = new SSHAgent(host);
                boolean available=metricStorage.availableHost(host.getId());
                if (sshAgent.connect()) {
                    if(!available) {//Если хост последний раз был не доступен, то выставляем дату окончания данного статуса
                        metricStorage.setAvailableHost(dateFormat.format(new Date()), host.getId());
                    }
                    for (final InstanceMetric instanceMetric : metricStorage.getInstMetrics(host.getId())) {
                        final String date = dateFormat.format(new Date());
                        final double valueMetric = sshAgent.getMetricValue(instanceMetric);

                        if(valueMetric!=(Integer.MIN_VALUE)) {
                            System.out.println("oK!");
                            Thread myThready = new Thread(new Runnable()
                            {
                                public void run()
                                {
                                    rageValue(valueMetric, instanceMetric, date);//min max
                                }
                            });
                            myThready.start();

                            if(!metricStorage.correctlyMetric(instanceMetric.getId()))                 //статус метрики OK
                                metricStorage.setCorrectlyMetric(date, instanceMetric.getId());

                            metricStorage.addValue(host.getId(), instanceMetric.getId(), valueMetric, date);
                        }
                        else {
                            System.out.println("unknow!");
                            if(metricStorage.correctlyMetric(instanceMetric.getId()))              //статус метрики ERR
                                metricStorage.setIncorrectlyMetric(date, instanceMetric.getId());
                        }
                    }
                } else {
                    System.out.println("not connect");
                    if(available) {//Если хост последний раз был доступен
                        metricStorage.setNotAvailableHost(dateFormat.format(new Date()), host.getId());
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void rageValue(double valueMetric,InstanceMetric instanceMetric,String date) {
        if (valueMetric > instanceMetric.getMaxValue()) {//MAX
            if (metricStorage.overMaxValue(instanceMetric.getId())){
                metricStorage.setOverMaxValue(date, instanceMetric.getId());
                System.out.println("overMax");
            }
        }

        if (valueMetric < instanceMetric.getMinValue()) {//MIN
            if (metricStorage.lessMinValue(instanceMetric.getId())) {
                metricStorage.setLessMinValue(date, instanceMetric.getId());
                System.out.println("lessMin");
            }
        }

        else//allowable
        {
            metricStorage.setAllowableValueMetric(date, instanceMetric.getId());
            System.out.println("Allowable");
        }
    }

}
