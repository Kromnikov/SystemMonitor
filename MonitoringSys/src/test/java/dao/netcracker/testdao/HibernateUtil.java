package dao.netcracker.testdao;
import java.io.File;

import net.core.configurations.SSHConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

/**
 * Created by ANTON on 01.04.2016.
 */



public class HibernateUtil {
    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration()
                    .configure()
                    .setProperty("hibernate.show_sql", "true")
                    .addAnnotatedClass(SSHConfiguration.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}