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

    public static void run() {
        context = new ClassPathXmlApplicationContext("META-INF/beans.xml");
    }
}
