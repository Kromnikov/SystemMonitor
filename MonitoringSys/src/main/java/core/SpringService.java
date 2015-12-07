package core;

import core.configurations.SSHConfiguration;
import core.hibernate.services.HostService;
import core.interfaces.db.IMetricStorage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Kromnikov on 30.11.2015.
 */
public class SpringService {

    private static ApplicationContext context;

    private static IMetricStorage metricStorage;

    private  static HostService hosts;

    public static void run() {
        context = new ClassPathXmlApplicationContext("META-INF/beans.xml");
        metricStorage=(context.getBean(IMetricStorage.class));
        hosts = (context.getBean(HostService.class));
    }

//    public static ApplicationContext getContext() {
//        return context;
//    }

    public static IMetricStorage getMetricStorage() {
        return metricStorage;
    }

    public static HostService getHosts() {
        return hosts;
    }

    public static void remove(SSHConfiguration content) {
        hosts.remove(content);

    }

    public static void save(SSHConfiguration content) {
        hosts.save(content);
    }
}
