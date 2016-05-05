package net.core.branches;

import net.core.agents.SSHAgent;
import net.core.alarms.RouteAlarms;
import net.core.alarms.dao.GenericAlarmDao;
import net.core.configurations.SSHConfiguration;
import net.core.IStorageController;
import net.core.hibernate.services.HostService;
import net.core.models.InstanceMetric;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

@Service("ScheduledTask")
public class ScheduledTask extends TimerTask {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final Logger logger = Logger.getLogger(ScheduledTask.class);

    private IStorageController metricStorage;

    @Autowired
    private RouteAlarms routeAlarms;

    private HostService hosts;

    private GenericAlarmDao genericAlarm;

    public ScheduledTask() {
    }

    @Autowired
    public ScheduledTask(IStorageController metricStorage,HostService hosts,GenericAlarmDao genericAlarm) {
        this.genericAlarm = genericAlarm;
        this.hosts=hosts;
        this.metricStorage=metricStorage;
    }

    @Scheduled(fixedDelay = 10000)
    @Override
    public void run() {
        System.out.println("ScheduledTask");

//        routeAlarms.sendMessage(genericAlarm.getByMetric(12), "Метрика lol");

//        routeAlarms.sendMessage(genericAlarm.getByHost(1));
//        System.out.println(alarmsLogDao.get(1).getMessage());
//        for (AlarmsLog g : alarmsLogDao.getByUser("anton")) {
//            System.out.println(g.getMessage());
//        }

        try {
            for (SSHConfiguration host : hosts.getAll()) {
                SSHAgent sshAgent = new SSHAgent(host);
                boolean available=metricStorage.availableHost(host.getId());
                if (sshAgent.connect()) {
                    if(!available) {//Если хост последний раз был не доступен, то выставляем дату окончания данного статуса
                        metricStorage.setAvailableHost(dateFormat.format(new Date()), host.getId());
                        Thread myThready = new Thread(new Runnable()
                        {
                            public void run()
                            {
                                routeAlarms.sendMessage("ok",genericAlarm.getByHost(host.getId()),"Соединение с хостом '"+host.getName()+"' было востановлено ");
                            }
                        });
                        myThready.start();
                    }
                    for (final InstanceMetric instanceMetric : metricStorage.getInstMetrics(host.getId())) {
                        final String date = dateFormat.format(new Date());
                        final double valueMetric = sshAgent.getMetricValue(instanceMetric);

                        if(valueMetric!=(Integer.MIN_VALUE)) {
                            Thread myThready = new Thread(new Runnable()
                            {
                                public void run()
                                {
                                    rageValue(valueMetric, instanceMetric, date ,host.getId());//min max
                                }
                            });
                            myThready.start();

                            if(!metricStorage.correctlyMetric(instanceMetric.getId()))                 //статус метрики OK
                                metricStorage.setCorrectlyMetric(date, instanceMetric.getId());

                            metricStorage.addValue(host.getId(), instanceMetric.getId(), valueMetric, date);
                        }
                        else {
                            if(metricStorage.correctlyMetric(instanceMetric.getId()))              //статус метрики ERR
                                metricStorage.setIncorrectlyMetric(date, instanceMetric.getId());
                        }
                    }
                } else {
//                    System.out.println("not connect");
                    if(available) {//Если хост последний раз был доступен
                        metricStorage.setNotAvailableHost(dateFormat.format(new Date()), host.getId(),host.getHost());
                        Thread myThready = new Thread(new Runnable()
                        {
                            public void run()
                            {
                                routeAlarms.sendMessage("error",genericAlarm.getByHost(host.getId()),"Cоединение с хостом "+host.getName()+ " было потеряно!");
                            }
                        });
                        myThready.start();

//                        MailAgent mailAgent = new MailAgent();
//                        mailAgent.send("Cоединение с хостом "+host.getHost()+ " было потеряно!","anton130794@ya.ru");

                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void rageValue(double valueMetric,InstanceMetric instanceMetric,String date,int hostId) {
        //TODO Сейчас может быть только больше или меньше, сразу два выхода не будет, можно это исправить
        if (valueMetric > instanceMetric.getMaxValue()) {//MAX
            if (metricStorage.overMaxValue(instanceMetric.getId())) {
                if (!metricStorage.lessMinValue(instanceMetric.getId())) {
                    metricStorage.setAllowableValueMetric(date, instanceMetric.getId());
                    routeAlarms.sendMessage("ok",genericAlarm.getByMetric(instanceMetric.getId()), "Метрика " + instanceMetric.getTitle() + " (Хост:" + hosts.get(instanceMetric.getHostId()).getName() + ") востановила своё значение! ");
                }
                metricStorage.setOverMaxValue(date, instanceMetric, hostId, valueMetric);
                routeAlarms.sendMessage("error",genericAlarm.getByMetric(instanceMetric.getId()), "Метрика " + instanceMetric.getTitle() + " (Хост:" + hosts.get(instanceMetric.getHostId()).getName() + ") вышла за допустимы диапазон! " +
                        "\nПорог максимального значения: " + instanceMetric.getMaxValue() +
                        "\nЗначение: " + valueMetric);
            }
        } else if (valueMetric < instanceMetric.getMinValue()) {//MIN
            if (metricStorage.lessMinValue(instanceMetric.getId())) {
                if (!metricStorage.overMaxValue(instanceMetric.getId())) {
                    metricStorage.setAllowableValueMetric(date, instanceMetric.getId());
                    routeAlarms.sendMessage("ok",genericAlarm.getByMetric(instanceMetric.getId()), "Метрика " + instanceMetric.getTitle() + " (Хост:" + hosts.get(instanceMetric.getHostId()).getName() + ") востановила своё значение! ");
                }
                metricStorage.setLessMinValue(date, instanceMetric, hostId, valueMetric);
                routeAlarms.sendMessage("error",genericAlarm.getByMetric(instanceMetric.getId()), "Метрика " + instanceMetric.getTitle() + " (Хост:" + hosts.get(instanceMetric.getHostId()).getName() + ") вышла за допустимы диапазон! " +
                        "\nПорог минимального значения: " + instanceMetric.getMinValue() +
                        "\nЗначение: " + valueMetric);
            }
        } else//allowable
        {
            if (metricStorage.isMetricHasProblem(instanceMetric.getId())) {
                metricStorage.setAllowableValueMetric(date, instanceMetric.getId());
                routeAlarms.sendMessage("ok",genericAlarm.getByMetric(instanceMetric.getId()), "Метрика " + instanceMetric.getTitle() + " (Хост:" + hosts.get(instanceMetric.getHostId()).getName() + ") востановила своё значение! ");
            }
        }
    }

}
