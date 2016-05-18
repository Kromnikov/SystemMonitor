package net.core.db;


import net.core.alarms.dao.AlarmsLogDao;
import net.core.alarms.dao.GenericAlarmDao;
import net.core.db.interfaces.IMetricStateStorage;
import net.core.hibernate.services.HostService;
import net.core.models.InstanceMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        String sql = "UPDATE \"METRIC_STATE\" SET \"end_datetime\" = (TIMESTAMP '" + endTime + "')  where  inst_metric =? and \"end_datetime\" is null";
        jdbcTemplateObject.update(sql,instMetric);
    }
    @Transactional //MAX
    public boolean overMaxValue(long instMetric) {
        String sql = "SELECT  COUNT(*)>0 FROM \"METRIC_STATE\" where  inst_metric =? and \"end_datetime\" is null and state like '%превысило%'";
        return jdbcTemplateObject.queryForObject(sql,Boolean.class,instMetric);

    }
    @Transactional
    public void setOverMaxValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric) {
        String state = "'Значение " + valueMetric + " превысило пороговое значение " + instanceMetric.getMaxValue() + "'";
        String sql = "INSERT INTO \"METRIC_STATE\"(start_datetime,state,inst_metric,resolved,host_id) " +
                " VALUES ((TIMESTAMP '" + startTime + "'),?,?,?,?)";
        jdbcTemplateObject.update(sql,state,instanceMetric.getId(),false,hostId);
    }
    @Transactional //MIN
    public boolean lessMinValue(long instMetric) {
        String sql = "SELECT  COUNT(*)>0 FROM \"METRIC_STATE\" where  inst_metric =? and \"end_datetime\" is null and state like '%ниже%'";
        return jdbcTemplateObject.queryForObject(sql,Boolean.class,instMetric);

    }
    @Transactional  //MIN
    public void setLessMinValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric) {
        String state = "'Значение " + valueMetric + " ниже порогового значения " + instanceMetric.getMinValue() + "'";
        String sql = "INSERT INTO \"METRIC_STATE\"(start_datetime,state,inst_metric,resolved,host_id) " +
                " VALUES ((TIMESTAMP '" + startTime + "'),?,?,?,?)";
        jdbcTemplateObject.update(sql,state,instanceMetric.getId(),false,hostId);
    }
    @Transactional
    public boolean correctlyMetric(long instMetric) {
        String sql = "SELECT  COUNT(*)>0  FROM \"METRIC_STATE\" where state='unknow' and inst_metric =? and \"end_datetime\" is null";
        return jdbcTemplateObject.queryForObject(sql,Boolean.class,instMetric);
    }
    @Transactional
    public void setCorrectlyMetric(String endTime, int instMetric) {
        String sql = "UPDATE \"METRIC_STATE\" SET \"end_datetime\" = (TIMESTAMP '" + endTime + "')  where state='unknow' and  inst_metric =? and \"end_datetime\" is null";
        jdbcTemplateObject.update(sql,instMetric);
    }
    @Transactional
    public void setIncorrectlyMetric(String startTime, int instMetric) {
        String sql = "INSERT INTO \"METRIC_STATE\"(start_datetime,state,inst_metric,resolved)  VALUES ((TIMESTAMP '" + startTime + "'),'unknow',?,?)";
        jdbcTemplateObject.update(sql,instMetric,false);
    }
}
