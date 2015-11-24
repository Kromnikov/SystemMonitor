package core.branches;

import core.agents.SQL.SQLAgent;
import core.agents.SSH.Metric;
import core.configurations.SQLConfiguration;
import core.configurations.SSHConfiguration;
import core.hibernate.HibernateUtil2;
import org.hibernate.Session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SQLBranch {

    private static SQLAgent sqlAgent;

    private static List<SSHConfiguration> hosts;

    private static Session session;

    public static void run() throws SQLException {
        SQLConfiguration sql = new SQLConfiguration();
        if(sql.load()) {
            sqlAgent = new SQLAgent(sql.getStatement());
        }
        session = HibernateUtil2.getSessionFactory().openSession();
        hosts = session.createCriteria(SSHConfiguration.class).list();
    }

    public static List<SSHConfiguration> getHosts() {
        return hosts;
    }

    public static void shutdown() {
        HibernateUtil2.shutdown();
    }
//sql
    //host
    public  static void addHost(String host,int port,String login,String password) {
        session.beginTransaction();
        session.save(new SSHConfiguration(host, port, login, password));
        session.getTransaction().commit();
    }
    public static SSHConfiguration getHost(int id) {
        return (SSHConfiguration) session.get(SSHConfiguration.class, id);
    }

    //values
    public static void addValue(int host,int metric,double value) throws SQLException {
        sqlAgent.addValue(host, metric, value);
    }

    //metrics
    public void addMetric(String title,String query) throws SQLException {
        sqlAgent.addMetric(title, query);
    }
    public static Metric getMetric(int id) throws SQLException {
        return sqlAgent.getMetric(id);
    }
    public static Metric getMetric(String title) throws SQLException {
        return sqlAgent.getMetric(title);
    }

    //metrics-host
    public void addMetricToHost(int host,int metric) throws SQLException {
        sqlAgent.addMetricToHost(host,metric);
    }
    public void addMetricToHost(SSHConfiguration host,Metric metric) throws SQLException {
        sqlAgent.addMetricToHost(host, metric);
    }
    public static List<Integer> getMetricIdByHostId(int hostId) throws SQLException {
        return sqlAgent.getMetricIdByHostId(hostId);
    }
    public static List<Metric> getMetricsByHostId(int hostId) throws SQLException {
        return sqlAgent.getMetricsByHostId(hostId);
    }
    public static int getQuantityOfRow(int id) throws SQLException {
        return sqlAgent.getQuantityOfRow(id);
    }
    public static ResultSet getAllValue(int id) throws SQLException {
        return sqlAgent.getAllValue(id);
    }

    public static void deleteHost(String login){

    }
}
