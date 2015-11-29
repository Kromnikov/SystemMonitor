package core.agents.sql;


import core.interfaces.SQLAgentInterface;
import core.models.Metric;
import core.models.Value;
import core.configurations.SSHConfiguration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public  class SQLAgent implements SQLAgentInterface{

    private Statement statement;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
    public void addValue(int host, int metric, double value,String dateTime) throws SQLException {
        String sql = "INSERT INTO \"VALUE_METRIC\"(host, metric, value,date_time)  VALUES ("+host+","+metric+","+value+",(TIMESTAMP '"+dateTime+"'))";
        this.statement.executeUpdate(sql);
    }
    public List<Double> getAllValueMetricOnHost(int id) throws SQLException {
        List<Double> values = new ArrayList<>();
        String sql = "select h.value from \"VALUE_METRIC\" as h join \"METRICS\" as m on h.metric=m.id where m.id=" + id;
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            values.add(Double.parseDouble(resultSet.getString(1)));
        }
        return values;
    }
    public ResultSet getAllValueMetricOnHostResult(int id) throws SQLException {
        String sql = "select h.value from \"VALUE_METRIC\" as h join \"METRICS\" as m on h.metric=m.id where m.id=" + id;
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;
    }

    public List<Value> getValues(int host_id,int metricId) throws SQLException {
        List<Value> values = new ArrayList<>();
        String sql = "select host,metric,value,date_time from \"VALUE_METRIC\" where metric=" + metricId+" and host ="+host_id;
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            values.add(
                    new Value(Integer.parseInt(resultSet.getString(1)),
                            Integer.parseInt(resultSet.getString(2)),
                                Double.parseDouble(resultSet.getString(3)),
                            LocalDateTime.parse((resultSet.getString(4)), formatter)
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

    public List<Metric> geAllMetrics() throws SQLException {
        List<Metric> metrics1 = new ArrayList<>();
        String sql = "SELECT * from \"METRICS\"";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            metrics1.add(new Metric(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),resultSet.getString(3)));
        }
        return metrics1;
    }

    public Integer getMetricID(String title) throws SQLException {
        Metric metric = new Metric();
        String sql = "select id from \"METRICS\" where title='"+title+"'";
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        int id = Integer.parseInt(resultSet.getString(1));
        return id;
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

    public ResultSet getAllValue(int id) throws SQLException {
        String sql = "select h.value from \"VALUE_METRIC\" as h join \"METRICS\" as m on h.metric=m.id where m.id=" + id;
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;
    }
    public List<String> getListIP() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "select host from \"sshconfigurationhibernate\"";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            list.add(resultSet.getString(1));
        }
        return list;
    }
    public int getHostIDbyTitle(String title) throws SQLException {
        String sql = "select sshconfigurationhibernate_id from \"sshconfigurationhibernate\" where host='"+title+"'";
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        return Integer.parseInt(resultSet.getString(1));
    }
    public void addStandartMetrics(int id) throws SQLException {
        String sql = "INSERT INTO \"HOST_METRIC\" VALUES ("+id+",1);";
        statement.executeUpdate(sql);
        String sql1 = "INSERT INTO \"HOST_METRIC\" VALUES ("+id+",2);";
        statement.executeUpdate(sql1);
        String sql2 = "INSERT INTO \"HOST_METRIC\" VALUES ("+id+",5);";
        statement.executeUpdate(sql2);
    }

    //delete-запросы
    public void delHost(String host) throws SQLException {
        String sql ="delete from  \"sshconfigurationhibernate\" where host='"+host+"'";
        this.statement.executeUpdate(sql);
    }
    public void delMetricFromHost(int id) throws SQLException {
        String sql ="delete from  \"HOST_METRIC\" where metric_id="+id;
        this.statement.executeUpdate(sql);
    }

    //
}
