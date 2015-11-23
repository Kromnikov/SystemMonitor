package core.hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;


public class HibernateUtil {
    Session session;
    Statement st;
    Properties prop = new Properties();
    InputStream input = null;

    public HibernateUtil() throws Exception{
        prop.load(new FileInputStream("config.properties"));

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        session = sessionFactory.openSession();

        Class.forName(prop.getProperty("ConfigDriver"));
        System.out.println("Driver Loaded.");

        String url = prop.getProperty("ConfigUrl");
        String login = prop.getProperty("Configdbuser");
        String password = prop.getProperty("Configdbpassword");

        Connection conn = DriverManager.getConnection(url, login, password);
        System.out.println("Got Connection.");
        st = conn.createStatement();
    }

    public Session getSession(){
        return session;
    }

    public void executeSQLCommand(String sql) throws Exception {
        st.executeUpdate(sql);
    }

    public void checkData(String sql) throws Exception {
        ResultSet rs = st.executeQuery(sql);
        ResultSetMetaData metadata = rs.getMetaData();

        for (int i = 0; i < metadata.getColumnCount(); i++) {
            System.out.print("\t"+ metadata.getColumnLabel(i + 1));
        }
        System.out.println("\n----------------------------------");

        while (rs.next()) {
            for (int i = 0; i < metadata.getColumnCount(); i++) {
                Object value = rs.getObject(i + 1);
                if (value == null) {
                    System.out.print("\t       ");
                } else {
                    System.out.print("\t"+value.toString().trim());
                }
            }
            System.out.println("");
        }
    }
}
