package core.factory;

import core.configurations.SSHConfiguration;
import core.interfaces.SQLAgentInterface;
import core.models.Metric;
import core.models.Value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Алексей on 25.11.2015.
 */
public class SQLAgentFactory {
    SQLAgentInterface sql ;

    public SQLAgentFactory(SQLAgentInterface sql) {
        this.sql = sql;
    }

    public Statement getStatement() {
        return sql.getStatement();
    }

    public void setStatement(Statement statement) {
        sql.setStatement(statement);
    }
//sql

    //values
    public void addValue(int host, int metric, double value,String dateTime) throws SQLException {
        sql.addValue(host,metric,value,dateTime);
    }

    public List<Double> getAllValueMetricOnHost(int id) throws SQLException {
        return sql.getAllValueMetricOnHost(id);
    }
    public List<Value> getValues(int host_id,int metricId) throws SQLException {
        return sql.getValues(host_id, metricId);
    }

    //metrics
    public void addMetric(String title,String query) throws SQLException {
        sql.addMetric(title,query);
    }
    public Metric getMetric(int id) throws SQLException {
        return sql.getMetric(id);
    }
    public Metric getMetric(String title) throws SQLException {
        return sql.getMetric(title);
    }

    //metrics-host
    public void addMetricToHost(int host,int metric) throws SQLException {
        sql.addMetricToHost(host, metric);
    }
    public void addMetricToHost(SSHConfiguration host,Metric metric) throws SQLException {
        sql.addMetricToHost(host,metric);
    }
    public List<Integer> getMetricIdByHostId(int hostId) throws SQLException {
        return sql.getMetricIdByHostId(hostId);
    }
    public List<Metric> getMetricsByHostId(int hostId) throws SQLException {
        return sql.getMetricsByHostId(hostId);
    }
    public int getQuantityOfRow(int id) throws SQLException {
        return sql.getQuantityOfRow(id);
    }

    public List<String> getListIP() throws SQLException {
        return sql.getListIP();
    }

    public ResultSet getAllValueMetricOnHostResult(int id)throws SQLException {
        return sql.getAllValueMetricOnHostResult(id);
    }

    public void delHost(String host) throws SQLException {
        sql.delHost(host);
    }

    public void delMetricFromHost(int id) throws SQLException{
        sql.delMetricFromHost(id);
    }

    public Integer getMetricID(String title) throws SQLException {
        return sql.getMetricID(title);
    }

    public int getHostIDbyTitle(String title) throws SQLException{
        return sql.getHostIDbyTitle(title);
    }


    public void addStandartMetrics(int id) throws SQLException{
        sql.addStandartMetrics(id);
    }

    public List<Metric> geAllMetrics() throws  SQLException{
       return sql.geAllMetrics();
    }
}
