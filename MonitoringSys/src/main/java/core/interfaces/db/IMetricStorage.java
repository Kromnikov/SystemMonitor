package core.interfaces.db;

import core.configurations.SSHConfiguration;
import core.models.Metric;
import core.models.Value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Алексей on 25.11.2015.
 */
public interface IMetricStorage {

    public Statement getStatement();

    public void setStatement(Statement statement);
//sql

    //values
    public void addValue(int host, int metric, double value,String dateTime) throws SQLException ;
    public List<Double> getAllValueMetricOnHost(int id) throws SQLException;
    public List<Value> getValues(int host_id,int metricId) throws SQLException ;

    //metrics
    public void addMetric(String title,String query) throws SQLException;
    public Metric getMetric(int id) throws SQLException;
    public Metric getMetric(String title) throws SQLException;

    //metrics-host
    public void addMetricToHost(int host,int metric) throws SQLException ;
    public void addMetricToHost(SSHConfiguration host,Metric metric) throws SQLException ;
    public List<Integer> getMetricIdByHostId(int hostId) throws SQLException ;
    public List<Metric> getMetricsByHostId(int hostId) throws SQLException;
    public int getQuantityOfRow(int id) throws SQLException;

    public List<String> getListIP() throws SQLException;
    public ResultSet getAllValueMetricOnHostResult(int id)throws SQLException;

    public void delHost(String host)throws SQLException ;

    public void delMetricFromHost(int id) throws SQLException;

    public Integer getMetricID(String title) throws  SQLException;

    public int getHostIDbyTitle(String title) throws SQLException;

    public void addStandartMetrics(int id) throws SQLException;

    List<Metric> geAllMetrics() throws SQLException;
}
