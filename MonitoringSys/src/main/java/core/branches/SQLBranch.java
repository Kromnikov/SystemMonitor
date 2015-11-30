package core.branches;

import core.MetricStorage;
import core.factory.db.FDBConnect;
import core.factory.db.FMetricStorage;
import core.models.Value;
import core.models.Metric;
import core.configurations.SQLConfiguration;
import core.configurations.SSHConfiguration;
import core.hibernate.HibernateUtil;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;

public class SQLBranch {

    private static FMetricStorage sqlAgent;

    private static List<SSHConfiguration> hosts;

    private static Session session;

    public static void run() throws SQLException {
        FDBConnect sql = new FDBConnect(new SQLConfiguration());
        if(sql.load()) {
            sqlAgent = new FMetricStorage(new MetricStorage(sql.getStatement()) );
        }
        session = HibernateUtil.getSessionFactory().openSession();
        hosts = session.createCriteria(SSHConfiguration.class).list();
    }

    public static List<SSHConfiguration> getHosts() {
        return hosts;
    }

    public static void shutdown() {
        HibernateUtil.shutdown();
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
    public static void addValue(int host,int metric,double value,String dateTime) throws SQLException {
        sqlAgent.addValue(host, metric, value , dateTime);
    }
    public static List<Double> getAllValueMetricOnHost(int id) throws SQLException {
        return sqlAgent.getAllValueMetricOnHost(id);
    }

    public static List<Value> getValues(int metricId,int host_id) throws SQLException {
        return sqlAgent.getValues(metricId, host_id);
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
    public static void addMetricToHost(int host,int metric) throws SQLException {
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
    public static List<String> getListIP() throws SQLException {
        return sqlAgent.getListIP();
    }

    public static void delHost(String host) throws SQLException {
        sqlAgent.delHost(host);
    }
    public static void delMetricFromHost(int id) throws SQLException {
        sqlAgent.delMetricFromHost(id);
    }
    public static Integer getMetricID(String title) throws SQLException {
        return sqlAgent.getMetricID(title);
    }

    public static int getHostIDbyTitle(String title) throws SQLException {
        return sqlAgent.getHostIDbyTitle(title);
    }
    public static void addStandartMetrics(int id) throws SQLException {
        sqlAgent.addStandartMetrics(id);
    }
    public static List<Metric> geAllMetrics() throws SQLException{
        return sqlAgent.geAllMetrics();
    }

}
