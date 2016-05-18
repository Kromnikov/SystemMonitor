package net.core.db;

import net.core.alarms.dao.AlarmsLogDao;
import net.core.alarms.dao.GenericAlarmDao;
import net.core.db.interfaces.IInstanceStorage;
import net.core.db.interfaces.IMetricProblemStorage;
import net.core.hibernate.services.HostService;
import net.core.models.MetricProblem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class MetricProblemStorage implements IMetricProblemStorage {

    @Autowired
    private IInstanceStorage instanceStorage;
    
    private JdbcTemplate jdbcTemplateObject;
    
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public MetricProblemStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    private MetricProblem getMetricProblem(Map row) {
        MetricProblem metricProblemTmp = new MetricProblem();
        metricProblemTmp.setId(Integer.parseInt(row.get("id").toString()));
        metricProblemTmp.setValue((row.get("state").toString()));
        try {
            metricProblemTmp.setStart(dateFormat.parse(row.get("start_datetime").toString()));
        if (row.get("end_datetime") != null) {
            metricProblemTmp.setEnd(dateFormat.parse(row.get("end_datetime").toString()));
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        metricProblemTmp.setInstMetric(instanceStorage.getInstMetric(Integer.parseInt(row.get("inst_metric").toString())).getTitle());
        metricProblemTmp.setResolved(Boolean.parseBoolean(row.get("resolved").toString()));

        return metricProblemTmp;
    }

    @Transactional
    public List<MetricProblem> getMetricProblems(int hostId) throws ParseException {
        List<MetricProblem> metricProblemList = new ArrayList<>();
        String sql = "SELECT * FROM \"METRIC_STATE\" where resolved = false and host_id=?";
        jdbcTemplateObject.queryForList(sql, hostId)
                .stream()
                .forEach(item->metricProblemList.add(getMetricProblem(item)));
        return metricProblemList;
    }

    @Transactional
    public List<MetricProblem> getMetricProblems(int hostId, int metricId) throws ParseException {
        List<MetricProblem> metricProblemList = new ArrayList<>();
        String sql = "SELECT * FROM \"METRIC_STATE\" where resolved = false and inst_metric=?";
        jdbcTemplateObject.queryForList(sql, metricId)
                .stream()
                .forEach(item->metricProblemList.add(getMetricProblem(item)));
        return metricProblemList;
    }

    @Transactional
    public List<MetricProblem> getMetricProblems() throws ParseException {
        List<MetricProblem> metricProblemList = new ArrayList<>();
        String sql = "SELECT *  FROM \"METRIC_STATE\" where resolved = false";
        jdbcTemplateObject.queryForList(sql)
                .stream()
                .forEach(item->metricProblemList.add(getMetricProblem(item)));
        return metricProblemList;
    }

    @Transactional
    public void setResolvedMetric(int id) {
        String sql = "UPDATE \"METRIC_STATE\" set resolved = true WHERE id =? and \"end_datetime\" is not null";
        jdbcTemplateObject.update(sql, id);
    }

    @Transactional
    public void setResolvedMetric() {
        String sql = "UPDATE \"METRIC_STATE\" set resolved = true WHERE \"end_datetime\" is not null";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public long getMetricNotResolvedLength() {
        String sql = "SELECT COUNT(*)  FROM \"METRIC_STATE\" where resolved = false";
        return (long) jdbcTemplateObject.queryForMap(sql).get("COUNT");
    }

    @Transactional
    public long getMetricNotResolvedLength(int hostId) {
        String sql = "SELECT COUNT(*)  FROM \"METRIC_STATE\" where resolved = false and host_id =?";
        return (long) jdbcTemplateObject.queryForMap(sql, hostId).get("COUNT");
    }
}
