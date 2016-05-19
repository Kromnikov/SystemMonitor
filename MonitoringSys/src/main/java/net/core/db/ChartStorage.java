package net.core.db;

import net.core.alarms.dao.AlarmsLogDao;
import net.core.alarms.dao.GenericAlarmDao;
import net.core.db.interfaces.IChartStorage;
import net.core.hibernate.services.HostService;
import net.core.models.GraphPoints;
import net.core.tools.Averaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class ChartStorage implements IChartStorage {
    private JdbcTemplate jdbcTemplateObject;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public ChartStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    private final String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between (?::timestamp) and (?::timestamp) and metric = ? and host = ? order by date_time ";

    @Transactional
    public void addValue(int host, int metric, double value, String dateTime) {
        String sql = "INSERT INTO \"VALUE_METRIC\"(host, metric, value,date_time)  VALUES (?,?,?,(TIMESTAMP ?))";
        jdbcTemplateObject.update(sql, host, metric, value, dateTime);
    }

    @Transactional
    public Date getLastDate(int hostId, int metricId) {
        String sql = "SELECT MAX(date_time) FROM \"VALUE_METRIC\" where metric = ? and host = ?";
        return jdbcTemplateObject.queryForObject(sql, Date.class, metricId,hostId);
    }

    @Transactional
    public GraphPoints getValuesLastDay(int host_id, int metricId, Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setHours(dateTime.getHours() - 24);

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql, dateFormat.format(nDate), dateFormat.format(dateTime), metricId, host_id);
        nDate.setSeconds(0);
        nDate.setMinutes(0);
        Map<Long, Object> map = Averaging.getValues(rows, nDate, "day");

        return new GraphPoints(new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesSixMonth(int host_id, int metricId, Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setMonth(dateTime.getMonth() - 3);
        dateTime.setMonth(dateTime.getMonth() + 3);

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql, dateFormat.format(nDate), dateFormat.format(dateTime), metricId, host_id);
        nDate.setSeconds(0);
        nDate.setMinutes(0);
        nDate.setHours(0);
        Map<Long, Object> map = Averaging.getValues(rows, nDate, "month");

        return new GraphPoints(new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesYear(int host_id, int metricId, Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setMonth(dateTime.getMonth() - 6);
        dateTime.setMonth(dateTime.getMonth() + 6);


        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql, dateFormat.format(nDate), dateFormat.format(dateTime), metricId, host_id);
        nDate = ((java.sql.Timestamp) rows.get(0).get("date_time"));
        nDate.setSeconds(0);
        nDate.setMinutes(0);
        nDate.setHours(0);
        Map<Long, Object> map = Averaging.getValues(rows, nDate, "year");
        return new GraphPoints(new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesMonth(int host_id, int metricId, Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setHours(dateTime.getHours() - 360);
        dateTime.setHours(dateTime.getHours() + 360);

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql, dateFormat.format(nDate), dateFormat.format(dateTime), metricId, host_id);
        nDate.setSeconds(0);
        nDate.setMinutes(0);
        nDate.setHours(0);
        Map<Long, Object> map = Averaging.getValues(rows, nDate, "month");

        return new GraphPoints(new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesTheeDays(int host_id, int metricId, Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setHours(dateTime.getHours() - 60);
        dateTime.setHours(dateTime.getHours() + 60);


        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql, dateFormat.format(nDate), dateFormat.format(dateTime), metricId, host_id);
        nDate.setSeconds(0);
        nDate.setMinutes(0);
        Map<Long, Object> map = Averaging.getValues(rows, nDate, "day");

        return new GraphPoints(new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesDay(int host_id, int metricId, Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setHours(dateTime.getHours() - 24);
        dateTime.setHours(dateTime.getHours() + 24);

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql, dateFormat.format(nDate), dateFormat.format(dateTime), metricId, host_id);
        nDate.setSeconds(0);
        nDate.setMinutes(0);
        Map<Long, Object> map = Averaging.getValues(rows, nDate, "day");

        return new GraphPoints(new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesLastHour(int host_id, int metricId, Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setMinutes(dateTime.getMinutes() - 30);
        dateTime.setMinutes(dateTime.getMinutes() + 30);

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql, dateFormat.format(nDate), dateFormat.format(dateTime), metricId, host_id);

        nDate.setSeconds(0);
        nDate.setMinutes(0);
        Map<Long, Object> map = Averaging.getValues(rows, nDate, "hour");
        return new GraphPoints(new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesTheeMinutes(int host_id, int metricId, Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setMinutes(dateTime.getMinutes() - 1);
        dateTime.setMinutes(dateTime.getMinutes() + 1);

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql, dateFormat.format(nDate), dateFormat.format(dateTime), metricId, host_id);
        Map<Long, Object> map = Averaging.getValues(rows, nDate, "minutes");
        return new GraphPoints(new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesOneMinutes(int host_id, int metricId, Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setSeconds(dateTime.getSeconds() - 30);
        dateTime.setSeconds(dateTime.getSeconds() + 30);

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql, dateFormat.format(nDate), dateFormat.format(dateTime), metricId, host_id);

        Map<Long, Object> map = Averaging.getValues(rows, nDate, "minutes");
        return new GraphPoints(new TreeMap<Long, Object>(map));
    }
}
