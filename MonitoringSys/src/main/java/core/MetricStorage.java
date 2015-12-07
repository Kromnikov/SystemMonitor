package core;


import core.configurations.SSHConfiguration;
import core.interfaces.db.IMetricStorage;
import core.models.Metric;
import core.models.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Service("MetricStorage")
public  class MetricStorage implements IMetricStorage {
    //@Autowired
    //private DataSource dataSource;

    private JdbcTemplate jdbcTemplateObject;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public MetricStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

//sql

    //values
    @Transactional
    public void addValue(int host, int metric, double value,String dateTime) throws SQLException {
        String sql = "INSERT INTO \"VALUE_METRIC\"(host, metric, value,date_time)  VALUES ("+host+","+metric+","+value+",(TIMESTAMP '"+dateTime+"'))";
        jdbcTemplateObject.update(sql);
    }
//    @Transactional
//    public List<Double> getAllValueMetricOnHost(int id) throws SQLException {
//        List<Double> values = new ArrayList<>();
//        String sql = "select h.value from \"VALUE_METRIC\" as h join \"METRICS\" as m on h.metric=m.id where m.id=" + id;
//        ResultSet resultSet = statement.executeQuery(sql);
//        while (resultSet.next()) {
//            values.add(Double.parseDouble(resultSet.getString(1)));
//        }
//        return values;
//    }
//    @Transactional
//    public ResultSet getAllValueMetricOnHostResult(int id) throws SQLException {
//        String sql = "select h.value from \"VALUE_METRIC\" as h join \"METRICS\" as m on h.metric=m.id where m.id=" + id;
//        return jdbcTemplateObject.queryForMap(sql);
//    }
    @Transactional
    public List<Value> getValues(int host_id,int metricId) throws SQLException {
        List<Value> values = new ArrayList<>();
        String sql = "select value,date_time from \"VALUE_METRIC\" where metric=" + metricId+" and host ="+host_id;
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            values.add(
                    new Value(
                                ((double)row.get("value")),
                            new java.util.Date( ((java.sql.Time)row.get("date_time")).getTime() )
                                    ));
        }
        return values;
    }

    //metrics
    @Transactional
    public void addMetric(String title,String query) throws SQLException {
        String sql = "INSERT INTO \"METRICS\"(title, query) VALUES ("+title+","+query+")";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public Metric getMetric(int id) throws SQLException {
        Metric metric = new Metric();
        String sql = "select * FROM \"TEMPLATE_METRICS\" where id ="+id;
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            metric.setId((int) row.get("id"));
            metric.setTitle((String) row.get("title"));
            metric.setCommand((String) row.get("query"));
        }
        return metric;
    }
    @Transactional
    public List<Metric> geAllMetrics() throws SQLException {
        List<Metric> metrics1 = new ArrayList<>();
        String sql = "SELECT * FROM \"TEMPLATE_METRICS\"";
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            Metric metric = new Metric();
            metric.setId((int) row.get("id"));
            metric.setTitle((String) row.get("title"));
            metric.setCommand((String) row.get("query"));
            metrics1.add(metric);
        }
        return metrics1;
    }
    @Transactional
    public Integer getMetricID(String title) throws SQLException {
        Metric metric = new Metric();
        String sql = "select id FROM \"TEMPLATE_METRICS\" where title='"+title+"'";
        return (int)jdbcTemplateObject.queryForMap(sql).get("id");
    }
    @Transactional
    public Metric getMetric(String title) throws SQLException {
        Metric metric = new Metric();
        String sql = "select * FROM \"TEMPLATE_METRICS\" where title ='"+title+"'";
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            metric.setId((int) row.get("id"));
            metric.setTitle((String) row.get("title"));
            metric.setCommand((String) row.get("query"));
        }
        return metric;
    }

    //metrics-host
    @Transactional
    public void addMetricToHost(int host,int metric) throws SQLException {
        String sql = "INSERT INTO \"HOST_METRIC\"(host_id, metric_id) VALUES ("+host+","+metric+")";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public void addMetricToHost(SSHConfiguration host,Metric metric) throws SQLException {
        String sql = "INSERT INTO \"HOST_METRIC\"(host_id, metric_id) VALUES ("+host.getId()+","+metric.getId()+")";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public List<Integer> getMetricIdByHostId(int hostId) throws SQLException {
        List<Integer> metrics = new ArrayList<>();
        String sql = "SELECT metric_id  FROM \"HOST_METRIC\" where host_id = "+hostId;
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            metrics.add((int)row.get("metric_id"));
        }
        return metrics;
    }
    @Transactional
    public List<String> getListIP() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "select host from \"sshconfigurationhibernate\"";
        List<Map<String,Object>> hosts = jdbcTemplateObject.queryForList(sql);
        for (Map host : hosts) {
            list.add((String)host.get("host"));
        }
       return list;
    }

    @Transactional
    public List<Metric> getMetricsByHostId(int hostId) throws SQLException {
        List<Metric> metrics = new ArrayList<>();
        String sql = "SELECT m.id,m.title, m.query  FROM \"TEMPLATE_METRICS\" as m left join \"HOST_METRIC\" as hm on hm.metric_id = m.id where hm.host_id ="+hostId;
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            Metric metric = new Metric();
            metric.setCommand((String) row.get("query"));
            metric.setTitle((String) row.get("title"));
            metric.setId((int) row.get("id"));
            metrics.add(metric);
        }
        return metrics;
    }
    @Transactional
    public long getQuantityOfRow(int id) throws SQLException {
        String sql = "select count(*) from \"VALUE_METRIC\" where metric ="+id;
        return (long)jdbcTemplateObject.queryForMap(sql).get("count");
    }

    @Transactional
    public int getHostIDbyTitle(String title) throws SQLException {
        String sql = "select sshconfigurationhibernate_id from \"sshconfigurationhibernate\" where host='"+title+"'";
        return (int)jdbcTemplateObject.queryForMap(sql).get("sshconfigurationhibernate_id");
    }

    @Transactional
    public void addStandartMetrics(int id) throws SQLException {
        String sql = "INSERT INTO \"HOST_METRIC\" VALUES ("+id+",1);";
        jdbcTemplateObject.update(sql);
        String sql1 = "INSERT INTO \"HOST_METRIC\" VALUES ("+id+",2);";
        jdbcTemplateObject.update(sql1);
        String sql2 = "INSERT INTO \"HOST_METRIC\" VALUES ("+id+",5);";
        jdbcTemplateObject.update(sql2);
    }

    //delete-запросы
    @Transactional
    public void delHost(String host) throws SQLException {
        String sql ="delete from  \"sshconfigurationhibernate\" where host='"+host+"'";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public void delMetricFromHost(int id) throws SQLException {
        String sql ="delete from  \"HOST_METRIC\" where metric_id="+id;
        jdbcTemplateObject.update(sql);
    }

    //
}
