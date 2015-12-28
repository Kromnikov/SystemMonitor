package com.core;


import core.configurations.SSHConfiguration;
import com.core.interfaces.db.IMetricStorage;
import com.core.models.InstanceMetric;
import com.core.models.TemplateMetric;
import com.core.models.Value;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
@Service("MetricStorage")
public  class MetricStorage implements IMetricStorage {

    private JdbcTemplate jdbcTemplateObject;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public MetricStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }


    //sql
    //metric-state
    @Transactional
    public void setAllowableValueMetric(String endTime, int instMetric) {
        String sql = "UPDATE \"METRIC_STATE\" SET \"end_datetime\" = (TIMESTAMP '"+endTime+"')  where (state='overMaxValue' or state='lessMinValue') and  inst_metric ="+instMetric+" and \"end_datetime\" is null";
        jdbcTemplateObject.update(sql);
    }

    @Transactional //MAX
    public boolean overMaxValue(long instMetric) {
        String sql = "SELECT id, state, start_datetime, \"end_datetime\", inst_metric  FROM \"METRIC_STATE\" where state='overMaxValue' and inst_metric =" + instMetric + " and \"end_datetime\" is null";
        boolean state = true;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return state;
        } else {
            return false;
        }
    }

    @Transactional
    public void setOverMaxValue(String startTime, int instMetric) {
        String sql = "INSERT INTO \"METRIC_STATE\"(start_datetime,state,inst_metric,resolved)  VALUES ((TIMESTAMP '" + startTime + "'),'overMaxValue'," + instMetric + ",false)";
        jdbcTemplateObject.update(sql);
    }


    @Transactional //MIN
    public boolean lessMinValue(long instMetric) {
        String sql = "SELECT id, state, start_datetime, \"end_datetime\", inst_metric  FROM \"METRIC_STATE\" where state='lessMinValue' and inst_metric =" + instMetric + " and \"end_datetime\" is null";
        boolean state = true;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return state;
        } else {
            return false;
        }
    }
    @Transactional  //MIN
    public void setLessMinValue(String startTime, int instMetric) {
        String sql = "INSERT INTO \"METRIC_STATE\"(start_datetime,state,inst_metric,resolved)  VALUES ((TIMESTAMP '" + startTime + "'),'lessMinValue'," + instMetric + ",false)";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public boolean correctlyMetric(long instMetric) {
        String sql = "SELECT id, state, start_datetime, \"end_datetime\", inst_metric  FROM \"METRIC_STATE\" where state='unknow' and inst_metric ="+instMetric+" and \"end_datetime\" is null";
        boolean state =true;
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return state;
        } else {
            return false;
        }
}
    @Transactional
    public void setCorrectlyMetric(String endTime, int instMetric) {
        String sql = "UPDATE \"METRIC_STATE\" SET \"end_datetime\" = (TIMESTAMP '"+endTime+"')  where state='unknow' and  inst_metric ="+instMetric+" and \"end_datetime\" is null";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public void setIncorrectlyMetric(String startTime, int instMetric) {
        String sql = "INSERT INTO \"METRIC_STATE\"(start_datetime,state,inst_metric,resolved)  VALUES ((TIMESTAMP '" + startTime + "'),'unknow'," + instMetric + ",false)";
        jdbcTemplateObject.update(sql);
    }


    //host-state
    @Transactional
    public boolean availableHost(long hostId) {//Нужен запрос на вывод состояния хоста
        String sql = "SELECT id, resolved, start_datetime, \"end_datetime\", host  FROM \"HOST_STATE\" where host = "+hostId+" and \"end_datetime\" is null";
        boolean state =true;
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return state;
        } else {
            for (Map row : rows) {
                state = (boolean) row.get("resolved");
            }
        }
        return state;
    }
    @Transactional
    public void setNotAvailableHost(String startTime, int host) {
        String sql = "INSERT INTO \"HOST_STATE\"(start_datetime,resolved,host)  VALUES ((TIMESTAMP '"+startTime+"'),false,"+host+")";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public void setAvailableHost(String endTime, int host) {
        String sql = "UPDATE \"HOST_STATE\" SET \"end_datetime\" = (TIMESTAMP '"+endTime+"')  where host ="+host+" and \"end_datetime\" is null";
        jdbcTemplateObject.update(sql);
    }


    //values
    @Transactional
    public void addValue(int host, int metric, double value,String dateTime) throws SQLException {
        String sql = "INSERT INTO \"VALUE_METRIC\"(host, metric, value,date_time)  VALUES ("+host+","+metric+","+value+",(TIMESTAMP '"+dateTime+"'))";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public List<Value> getValues(int host_id,int metricId) throws SQLException {
        List<Value> values = new ArrayList<>();
        String sql = "select value,date_time from \"VALUE_METRIC\" where metric=" + metricId+" and host ="+host_id;
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            values.add(
                    new Value(
                                ((double)row.get("value")),
                            new java.util.Date( ((java.sql.Timestamp)row.get("date_time")).getTime() )
                                    ));
        }
        return values;
    }
    @Transactional
    public List<Value> getValuesLastHour(int host_id,int metricId,Date dateTime) {
        List<Value> values = new ArrayList<>();
        Date nDate = (Date)dateTime.clone();
        nDate.setHours(dateTime.getHours()-1);
        String sql = "SELECT avg(value),date_time FROM \"VALUE_METRIC\"  where date_time between  '"+dateFormat.format(nDate)+"' and '"+dateFormat.format(dateTime)+"' and metric = "+metricId+" and host = "+host_id+"  GROUP BY  date_time  order by date_time ";
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            values.add(
                    new Value(
                            ((double)row.get("avg")),
                            new java.util.Date( ((java.sql.Timestamp)row.get("date_time")).getTime() )
                    ));
        }
        double sumValues = 0,countValues = 0;
        sumValues += values.get(0).getValue();
        countValues++;
        for (int i = 1; i < values.size(); i++) {
            if ((values.get(i).getDateTime().getHours() == values.get(i - 1).getDateTime().getHours()) & (values.get(i).getDateTime().getMinutes() == values.get(i - 1).getDateTime().getMinutes())) {
//                if (values.get(i).getDateTime().getMinutes() == values.get(i - 1).getDateTime().getMinutes()) {
                sumValues += values.get(i).getValue();
                countValues++;
//                }
            } else {

            }
        }




        return  values;
    }



    //metrics
    @Transactional
    public void addTemplateMetric(String title, String query) throws SQLException {
        String sql = "INSERT INTO \"TEMPLATE_METRICS\"(title, query) VALUES ("+title+","+query+")";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public TemplateMetric getTemplateMetric(int id) throws SQLException {
        TemplateMetric templateMetric = new TemplateMetric();
        String sql = "select * FROM \"TEMPLATE_METRICS\" where id ="+id;
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            templateMetric.setId((int) row.get("id"));
            templateMetric.setTitle((String) row.get("title"));
            templateMetric.setCommand((String) row.get("query"));
        }
        return templateMetric;
    }
    @Transactional
    public List<TemplateMetric> getTemplatMetrics() throws SQLException {
        List<TemplateMetric> metrics1 = new ArrayList<>();
        String sql = "SELECT * FROM \"TEMPLATE_METRICS\"";
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            TemplateMetric templateMetric = new TemplateMetric();
            templateMetric.setId((int) row.get("id"));
            templateMetric.setTitle((String) row.get("title"));
            templateMetric.setCommand((String) row.get("query"));
            metrics1.add(templateMetric);
        }
        return metrics1;
    }
    @Transactional
    public Integer getTemplatMetricID(String title) throws SQLException {
        TemplateMetric templateMetric = new TemplateMetric();
        String sql = "select id FROM \"TEMPLATE_METRICS\" where title='"+title+"'";
        return (int)jdbcTemplateObject.queryForMap(sql).get("id");
    }
    @Transactional
    public TemplateMetric getTemplatMetric(String title) throws SQLException {
        TemplateMetric templateMetric = new TemplateMetric();
        String sql = "select * FROM \"TEMPLATE_METRICS\" where title ='"+title+"'";
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            templateMetric.setId((int) row.get("id"));
            templateMetric.setTitle((String) row.get("title"));
            templateMetric.setCommand((String) row.get("query"));
        }
        return templateMetric;
    }



    //metrics-host
    @Transactional
    public void addInstMetric(int host, int metric) throws SQLException {
        TemplateMetric templateMetric = getTemplateMetric(metric);
        String sql = "INSERT INTO \"INSTANCE_METRIC\"(host, templ_metric,min_value,max_value,title,query) VALUES (" + host + "," + metric + ",0,0,'"+templateMetric.getTitle()+"',$q$"+templateMetric.getCommand()+"$q$)";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public void addInstMetric(SSHConfiguration host, TemplateMetric templateMetric) throws SQLException {
        String sql = "INSERT INTO \"INSTANCE_METRIC\"(host, templ_metric,min_value,max_value,title,query) VALUES (" + host.getId() + "," + templateMetric.getId() + ",0,0,'"+templateMetric.getTitle()+"',$q$"+templateMetric.getCommand()+"$q$)";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public void addInstMetric(InstanceMetric instanceMetric) throws SQLException {
        String sql = "INSERT INTO \"INSTANCE_METRIC\"(host, templ_metric,min_value,max_value,title,query) VALUES (" + instanceMetric.getHostId() + "," + instanceMetric.getTempMetrcId() + ","+instanceMetric.getMinValue()+","+instanceMetric.getMaxValue()+",'"+instanceMetric.getTitle()+"',$q$"+instanceMetric.getCommand()+"$q$)";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public List<InstanceMetric> getInstMetrics(int hostId) throws SQLException {
        List<InstanceMetric> instanceMetrics = new ArrayList<>();
        String sql = "SELECT id, templ_metric, title, query, min_value, max_value, host  FROM \"INSTANCE_METRIC\" where host ="+hostId;
        List<Map<String,Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            InstanceMetric instanceMetric = new InstanceMetric();
            instanceMetric.setId((int) row.get("id"));
            instanceMetric.setHostId(hostId);
            instanceMetric.setTempMetrcId((int) row.get("templ_metric"));
            instanceMetric.setMinValue((double) row.get("min_value"));
            instanceMetric.setMaxValue((double) row.get("max_value"));
            instanceMetric.setCommand((String) row.get("query"));
            instanceMetric.setTitle((String) row.get("title"));
            instanceMetrics.add(instanceMetric);
        }
        return instanceMetrics;
    }














    @Transactional
    public long getQuantityOfRow(int id) throws SQLException {
        String sql = "select count(*) from \"VALUE_METRIC\" where metric ="+id;
        return (long)jdbcTemplateObject.queryForMap(sql).get("count");
    }

    @Override
    public void delMetricFromHost(int id) throws SQLException {

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
//    @Transactional TODO удаление хостов должно быть через hibernate
//    public void delHost(String host) throws SQLException {
//        String sql ="delete from  \"sshconfigurationhibernate\" where host='"+host+"'";
//        jdbcTemplateObject.update(sql);
//    }
    @Transactional
    public void delMetricFromHost(int host,int id) throws SQLException {
        String sql ="delete from  \"HOST_METRIC\" where where metric_id="+id+" and host_id="+host;
        jdbcTemplateObject.update(sql);
    }
    public void delHost(String host) throws SQLException {
        String sql ="delete from  \"sshconfigurationhibernate\" where host='"+host+"'";
        jdbcTemplateObject.update(sql);
    }

    //
}
