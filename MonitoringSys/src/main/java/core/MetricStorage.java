package core;


import core.configurations.SSHConfiguration;
import core.interfaces.db.IMetricStorage;
import core.models.Metric;
import core.models.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service("MetricStorage")
public  class MetricStorage implements IMetricStorage {

    private Statement statement;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MetricStorage() {

    }

    public MetricStorage(Statement statement) {
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
    @Transactional
    public void addValue(int host, int metric, double value,String dateTime) throws SQLException {
        String sql = "INSERT INTO \"VALUE_METRIC\"(host, metric, value,date_time)  VALUES ("+host+","+metric+","+value+",(TIMESTAMP '"+dateTime+"'))";
        this.statement.executeUpdate(sql);
    }
    @Transactional
    public List<Double> getAllValueMetricOnHost(int id) throws SQLException {
        List<Double> values = new ArrayList<>();
        String sql = "select h.value from \"VALUE_METRIC\" as h join \"METRICS\" as m on h.metric=m.id where m.id=" + id;
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            values.add(Double.parseDouble(resultSet.getString(1)));
        }
        return values;
    }
    @Transactional
    public ResultSet getAllValueMetricOnHostResult(int id) throws SQLException {
        String sql = "select h.value from \"VALUE_METRIC\" as h join \"METRICS\" as m on h.metric=m.id where m.id=" + id;
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;
    }
    @Transactional
    public List<Value> getValues(int host_id,int metricId) throws SQLException {
        List<Value> values = new ArrayList<>();
        String sql = "select value,date_time from \"VALUE_METRIC\" where metric=" + metricId+" and host ="+host_id;
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            values.add(
                    new Value(
                                Double.parseDouble(resultSet.getString(1)),
                            Date.valueOf(resultSet.getString(2))
                                    ));
        }
        return values;
    }

    //metrics
    @Transactional
    public void addMetric(String title,String query) throws SQLException {
        String sql = "INSERT INTO \"METRICS\"(title, query) VALUES ("+title+","+query+")";
        this.statement.executeUpdate(sql);
    }
    @Transactional
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
    @Transactional
    public List<Metric> geAllMetrics() throws SQLException {
        List<Metric> metrics1 = new ArrayList<>();
        String sql = "SELECT * from \"METRICS\"";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            metrics1.add(new Metric(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),resultSet.getString(3)));
        }
        return metrics1;
    }
    @Transactional
    public Integer getMetricID(String title) throws SQLException {
        Metric metric = new Metric();
        String sql = "select id from \"METRICS\" where title='"+title+"'";
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        int id = Integer.parseInt(resultSet.getString(1));
        return id;
    }
    @Transactional
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
    @Transactional
    public void addMetricToHost(int host,int metric) throws SQLException {
        String sql = "INSERT INTO \"HOST_METRIC\"(host_id, metric_id) VALUES ("+host+","+metric+")";
        this.statement.executeUpdate(sql);
    }
    @Transactional
    public void addMetricToHost(SSHConfiguration host,Metric metric) throws SQLException {
        String sql = "INSERT INTO \"HOST_METRIC\"(host_id, metric_id) VALUES ("+host.getId()+","+metric.getId()+")";
        this.statement.executeUpdate(sql);
    }
    @Transactional
    public List<Integer> getMetricIdByHostId(int hostId) throws SQLException {
        List<Integer> metrics = new ArrayList<>();
        String sql = "SELECT metric_id  FROM \"HOST_METRIC\" where host_id = "+hostId;
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            metrics.add(Integer.parseInt(resultSet.getString(1)));
        }
        return metrics;
    }
    @Transactional
    public List<Metric> getMetricsByHostId(int hostId) throws SQLException {
        List<Metric> metrics = new ArrayList<>();
        String sql = "SELECT m.id,m.title, m.query  FROM \"METRICS\" as m left join \"HOST_METRIC\" as hm on hm.metric_id = m.id where hm.host_id ="+hostId;
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            metrics.add(new Metric(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),resultSet.getString(3)));
        }
        return metrics;
    }
    @Transactional
    public int getQuantityOfRow(int id) throws SQLException {
        String sql = "select count(*) from \"VALUE_METRIC\" where metric ="+id;
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        return Integer.parseInt(resultSet.getString(1));
    }
    @Transactional
    public ResultSet getAllValue(int id) throws SQLException {
        String sql = "select h.value from \"VALUE_METRIC\" as h join \"METRICS\" as m on h.metric=m.id where m.id=" + id;
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;
    }
    @Transactional
    public List<String> getListIP() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "select host from \"sshconfigurationhibernate\"";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            list.add(resultSet.getString(1));
        }
        return list;
    }
    @Transactional
    public int getHostIDbyTitle(String title) throws SQLException {
        String sql = "select sshconfigurationhibernate_id from \"sshconfigurationhibernate\" where host='"+title+"'";
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        return Integer.parseInt(resultSet.getString(1));
    }
    @Transactional
    public void addStandartMetrics(int id) throws SQLException {
        String sql = "INSERT INTO \"HOST_METRIC\" VALUES ("+id+",1);";
        statement.executeUpdate(sql);
        String sql1 = "INSERT INTO \"HOST_METRIC\" VALUES ("+id+",2);";
        statement.executeUpdate(sql1);
        String sql2 = "INSERT INTO \"HOST_METRIC\" VALUES ("+id+",5);";
        statement.executeUpdate(sql2);
    }

    //delete-запросы
    @Transactional
    public void delHost(String host) throws SQLException {
        String sql ="delete from  \"sshconfigurationhibernate\" where host='"+host+"'";
        this.statement.executeUpdate(sql);
    }
    @Transactional
    public void delMetricFromHost(int id) throws SQLException {
        String sql ="delete from  \"HOST_METRIC\" where metric_id="+id;
        this.statement.executeUpdate(sql);
    }

    //
}
