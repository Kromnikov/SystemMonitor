package core.agents.sql;


import core.Models.Metric;
import core.Models.Value;
import core.configurations.SSHConfiguration;
import org.jfree.data.Values;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SQLAgent {

    private Statement statement;

    public SQLAgent() {

    }

    public SQLAgent(Statement statement) {
        this.statement = statement;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }
//sql

    //values
    public void addValue(int host, int metric, double value,LocalDateTime dateTime) throws SQLException {
        String sql = "INSERT INTO \"VALUE_METRIC\"(host, metric, value,date_time)  VALUES ("+host+","+metric+","+value+",(TIMESTAMP '"+dateTime+"'))";
        this.statement.executeUpdate(sql);
    }
    public List<Double> getAllValue(int id) throws SQLException {
        List<Double> values = new ArrayList<>();
        String sql = "select h.value from \"VALUE_METRIC\" as h join \"METRICS\" as m on h.metric=m.id where m.id=" + id;
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            values.add(Double.parseDouble(resultSet.getString(1)));
        }
        return values;
    }
    public List<Value> getValues(int metricId,int host_id) throws SQLException {
        List<Value> values = new ArrayList<>();
        String sql = "select * from \"VALUE_METRIC\" where metric=" + metricId+" and host ="+host_id;
        ResultSet resultSet = statement.executeQuery(sql);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        while (resultSet.next()) {
            values.add(
                    new Value(Integer.parseInt(resultSet.getString(1)),
                    Integer.parseInt(resultSet.getString(2)),
                            Integer.parseInt(resultSet.getString(3)),
                                Double.parseDouble(resultSet.getString(4)),
                            LocalDateTime.parse((resultSet.getString(5)), formatter)
                                    ));
        }
        return values;
    }

    //metrics
    public void addMetric(String title,String query) throws SQLException {
        String sql = "INSERT INTO \"METRICS\"(title, query) VALUES ("+title+","+query+")";
        this.statement.executeUpdate(sql);
    }
    public Metric getMetric(int id) throws SQLException {
        Metric metric = new Metric();
        String sql = "select * from \"METRICS\" where id ="+id;
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            metric.setId(Integer.parseInt(resultSet.getString(1)));
            metric.setTitle(resultSet.getString(2));
            metric.setCommand(resultSet.getString(3));
        }
        return metric;
    }
    public Metric getMetric(String title) throws SQLException {
        Metric metric = new Metric();
        String sql = "select * from \"METRICS\" where title ='"+title+"'";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            metric.setId(Integer.parseInt(resultSet.getString(1)));
            metric.setTitle(resultSet.getString(2));
            metric.setCommand(resultSet.getString(3));
        }
        return metric;
    }

    //metrics-host
    public void addMetricToHost(int host,int metric) throws SQLException {
        String sql = "INSERT INTO \"HOST_METRIC\"(host_id, metric_id) VALUES ("+host+","+metric+")";
        this.statement.executeUpdate(sql);
    }
    public void addMetricToHost(SSHConfiguration host,Metric metric) throws SQLException {
        String sql = "INSERT INTO \"HOST_METRIC\"(host_id, metric_id) VALUES ("+host.getId()+","+metric.getId()+")";
        this.statement.executeUpdate(sql);
    }
    public List<Integer> getMetricIdByHostId(int hostId) throws SQLException {
        List<Integer> metrics = new ArrayList<>();
        String sql = "SELECT metric_id  FROM \"HOST_METRIC\" where host_id = "+hostId;
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            metrics.add(Integer.parseInt(resultSet.getString(1)));
        }
        return metrics;
    }
    public List<Metric> getMetricsByHostId(int hostId) throws SQLException {
        List<Metric> metrics = new ArrayList<>();
        String sql = "SELECT m.id,m.title, m.query  FROM \"METRICS\" as m left join \"HOST_METRIC\" as hm on hm.metric_id = m.id where hm.host_id ="+hostId;
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            metrics.add(new Metric(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),resultSet.getString(3)));
        }
        return metrics;
    }
    public int getQuantityOfRow(int id) throws SQLException {
        String sql = "select count(*) from \"VALUE_METRIC\" where metric ="+id;
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        return Integer.parseInt(resultSet.getString(1));
    }

    //
}
