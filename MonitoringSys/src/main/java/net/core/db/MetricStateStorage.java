package net.core.db;


import net.core.db.interfaces.IMetricStateStorage;
import net.core.models.InstanceMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Repository
public class MetricStateStorage implements IMetricStateStorage{
    private JdbcTemplate jdbcTemplateObject;
    @Autowired
    public MetricStateStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Transactional //MAX
    public boolean isMetricHasProblem(long instMetric) {
        String sql = "SELECT COUNT(*)>0 FROM \"METRIC_STATE\" where  inst_metric =? and \"end_datetime\" is null ";
        return jdbcTemplateObject.queryForObject(sql,Boolean.class,instMetric);

    }
    @Transactional
    public void setAllowableValueMetric(String endTime, int instMetric) {
        String sql = "UPDATE \"METRIC_STATE\" SET \"end_datetime\" = (?::timestamp)  where  inst_metric =? and \"end_datetime\" is null";
        jdbcTemplateObject.update(sql,endTime,instMetric);
    }
    @Transactional //MAX
    public boolean overMaxValue(long instMetric) {
        String sql = "SELECT  COUNT(*)>0 FROM \"METRIC_STATE\" where  inst_metric =? and \"end_datetime\" is null and state like '%превысило%'";
        return jdbcTemplateObject.queryForObject(sql,Boolean.class,instMetric);

    }

    private String getStringOverMax(double valueMetric, double overValue) {
        StringBuilder sb = new StringBuilder("'Значение ");
        sb.append(valueMetric);
        sb.append(" превысило пороговое значение ");
        sb.append(overValue);
        sb.append("'");
        return sb.toString();
    }

    private String getStringLessMin(double valueMetric, double overValue) {
        StringBuilder sb = new StringBuilder("'Значение ");
        sb.append(valueMetric);
        sb.append(" ниже порогового значения ");
        sb.append(overValue);
        sb.append("'");
        return sb.toString();
    }

    @Transactional
    public void setOverMaxValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric) {
        String sql = "INSERT INTO \"METRIC_STATE\"(start_datetime,state,inst_metric,resolved,host_id) " +
                " VALUES ((?::timestamp),?,?,?,?)";
        jdbcTemplateObject.update(sql,startTime, getStringOverMax(valueMetric, instanceMetric.getMaxValue()),instanceMetric.getId(),false,hostId);
    }
    @Transactional //MIN
    public boolean lessMinValue(long instMetric) {
        String sql = "SELECT  COUNT(*)>0 FROM \"METRIC_STATE\" where  inst_metric =? and \"end_datetime\" is null and state like '%ниже%'";
        return jdbcTemplateObject.queryForObject(sql,Boolean.class,instMetric);

    }
    @Transactional  //MIN
    public void setLessMinValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric) {
        String sql = "INSERT INTO \"METRIC_STATE\"(start_datetime,state,inst_metric,resolved,host_id) " +
                " VALUES ((?::timestamp),?,?,?,?)";
        jdbcTemplateObject.update(sql,startTime,getStringLessMin(valueMetric,instanceMetric.getMinValue()),instanceMetric.getId(),false,hostId);
    }
    @Transactional
    public boolean correctlyMetric(long instMetric) {
        String sql = "SELECT  COUNT(*)>0  FROM \"METRIC_STATE\" where state='unknow' and inst_metric =? and \"end_datetime\" is null";
        return jdbcTemplateObject.queryForObject(sql,Boolean.class,instMetric);
    }
    @Transactional
    public void setCorrectlyMetric(String endTime, int instMetric) {
        String sql = "UPDATE \"METRIC_STATE\" SET \"end_datetime\" = (?::timestamp)  where state='unknow' and  inst_metric =? and \"end_datetime\" is null";
        jdbcTemplateObject.update(sql,endTime,instMetric);
    }
    @Transactional
    public void setIncorrectlyMetric(String startTime, int instMetric) {
        String sql = "INSERT INTO \"METRIC_STATE\"(start_datetime,state,inst_metric,resolved)  VALUES ((?::timestamp),'unknow',?,?)";
        jdbcTemplateObject.update(sql,startTime,instMetric,false);
    }
}
