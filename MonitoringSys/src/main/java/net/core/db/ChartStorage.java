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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class ChartStorage implements IChartStorage{
    private JdbcTemplate jdbcTemplateObject;
    @Autowired
    private AlarmsLogDao alarmsLogDao;
    @Autowired
    private HostService hosts;
    @Autowired
    private GenericAlarmDao genericAlarm;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public ChartStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }


    @Transactional
    public void addValue(int host, int metric, double value, String dateTime)  {
        String sql = "INSERT INTO \"VALUE_METRIC\"(host, metric, value,date_time)  VALUES (?,?,?,(TIMESTAMP '" + dateTime + "'))";
        jdbcTemplateObject.update(sql,host,metric,value);
    }
    @Transactional
    public Date getLastDate(int hostId, int metricId) {
        String sql = "SELECT MAX(date_time) FROM \"VALUE_METRIC\" where metric = ? and host = ?";
        return (Date) jdbcTemplateObject.queryForMap(sql,metricId,hostId).get("MAX");
    }

    @Transactional
    public GraphPoints getAllValues(int host_id, int metricId) {
        long defTime= 10*1000;//10sek
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where metric = ? and host = ? order by date_time ";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql,metricId,host_id);
        Map<Long, Object> map = new HashMap<>();
        long x=0,prevX=0,Dif=0;
        Object y=0,prevY=0;
        if (rows.size() > 0) {
            prevX = (long) (((java.sql.Timestamp) rows.get(0).get("date_time")).getTime());
            prevY = (double) rows.get(0).get("value");
            map.put(prevX, (double)prevY);
            for (int i = 1; i < rows.size(); i++) {
                x = (long) (((java.sql.Timestamp) rows.get(i).get("date_time")).getTime());
                y = (double) rows.get(i).get("value");
                Dif = x - prevX;
                while (Dif>defTime) {
                    prevX += defTime;
                    prevY = null;
                    Dif = x - prevX;
                    map.put(prevX, prevY);
                }
                map.put(x, y);
                prevX=x;
                prevY=y;
            }
        }
        return new GraphPoints(rows.size(), new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesLastDay(int host_id, int metricId, Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setHours(dateTime.getHours() - 24);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        Map<Long, Object> map = new HashMap<>();
        if (rowSize > 0) {
            nDate.setSeconds(0);
            nDate.setMinutes(0);
            map = Averaging.getValues(rows, nDate, "day");
        }

        return new GraphPoints(rowSize, new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesSixMonth(int host_id, int metricId,  Date dateTime) {
        Date nDate = (Date) dateTime.clone();
        nDate.setMonth(dateTime.getMonth() - 3);
        dateTime.setMonth(dateTime.getMonth() + 3);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        double values = (double) rows.get(0).get("value");
        Map<Long, Double> map = new HashMap<>();
        nDate = ((java.sql.Timestamp) rows.get(0).get("date_time"));
        nDate.setSeconds(0);
        nDate.setMinutes(0);
        nDate.setHours(0);
        for (int i = 1; i < rowSize; i++) {
            if ((nDate.getDay() == ((java.sql.Timestamp) rows.get(i).get("date_time")).getDay()) & (nDate.getMonth() == ((java.sql.Timestamp) rows.get(i).get("date_time")).getMonth())) {
                values += (double) rows.get(i).get("value");
                counter++;
            } else {
                map.put((long) nDate.getTime(), values / counter);
                nDate = ((java.sql.Timestamp) rows.get(i).get("date_time"));
                nDate.setSeconds(0);
                nDate.setMinutes(0);
                nDate.setHours(0);
                values = (double) rows.get(i).get("value");
                counter = 1;
            }
            map.put((long) nDate.getTime(), values / counter);
        }

        return new GraphPoints(rowSize, new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesYear(int host_id, int metricId,  Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setMonth(dateTime.getMonth() - 6);
        dateTime.setMonth(dateTime.getMonth() + 6);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        double values = (double) rows.get(0).get("value");
        nDate = ((java.sql.Timestamp) rows.get(0).get("date_time"));
        nDate.setSeconds(0);
        nDate.setMinutes(0);
        nDate.setHours(0);
        Map<Long, Object> map = new HashMap<>();
        if (rowSize > 0) {
            map = Averaging.getValues(rows, nDate, "year");
        }
        return new GraphPoints(rowSize, new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesMonth(int host_id, int metricId,  Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setHours(dateTime.getHours() - 360);
        dateTime.setHours(dateTime.getHours() + 360);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        Map<Long, Object> map = new HashMap<>();
        if (rowSize > 0) {
            nDate.setSeconds(0);
            nDate.setMinutes(0);
            nDate.setHours(0);
            map = Averaging.getValues(rows, nDate, "month");
        }

        return new GraphPoints(rowSize, new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesTheeDays(int host_id, int metricId,  Date dateTime) {
        Date nDate = (Date) dateTime.clone();
        nDate.setHours(dateTime.getHours() - 60);
        dateTime.setHours(dateTime.getHours() + 60);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        double values = (double) rows.get(0).get("value");
        Map<Long, Double> map = new HashMap<>();
        nDate = ((java.sql.Timestamp) rows.get(0).get("date_time"));
        nDate.setSeconds(0);
        nDate.setMinutes(0);
        for (int i = 1; i < rowSize; i++) {

            if ((nDate.getHours() == ((java.sql.Timestamp) rows.get(i).get("date_time")).getHours()) & (nDate.getDay() == ((java.sql.Timestamp) rows.get(i).get("date_time")).getDay())) {
                values += (double) rows.get(i).get("value");
                counter++;
            } else {
                map.put((long) nDate.getTime(), values / counter);
                nDate = ((java.sql.Timestamp) rows.get(i).get("date_time"));
                nDate.setSeconds(0);
                nDate.setMinutes(0);
                values = (double) rows.get(i).get("value");
                counter = 1;
            }
            map.put((long) nDate.getTime(), values / counter);
        }

        return new GraphPoints(rowSize, new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesDay(int host_id, int metricId,  Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setHours(dateTime.getHours() - 24);
        dateTime.setHours(dateTime.getHours() + 24);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        Map<Long, Object> map = new HashMap<>();
        if (rowSize > 0) {
            nDate.setSeconds(0);
            nDate.setMinutes(0);
            map = Averaging.getValues(rows, nDate, "day");
        }

        return new GraphPoints(rowSize, new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesLastHour(int host_id, int metricId,  Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setMinutes(dateTime.getMinutes() - 30);
        dateTime.setMinutes(dateTime.getMinutes() + 30);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        nDate.setSeconds(0);
        nDate.setMinutes(0);
        Map<Long, Object> map = new HashMap<>();

        if (rows.size() > 0) {
            map = Averaging.getValues(rows, nDate, "hour");
        }
        return new GraphPoints(rows.size(), new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesTheeMinutes(int host_id, int metricId,  Date dateTime) throws ParseException {
        long defTime= 10*1000;//10sek
        Date nDate = (Date) dateTime.clone();
        nDate.setMinutes(dateTime.getMinutes() - 1);
        dateTime.setMinutes(dateTime.getMinutes() + 1);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        Map<Long, Object> map = new HashMap<>();
        if (rows.size() > 0) {
            map = Averaging.getValues(rows, nDate, "minutes");
        }
        return new GraphPoints(rows.size(), new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesOneMinutes(int host_id, int metricId,  Date dateTime) throws ParseException {
        long defTime= 10*1000;//10sek
        Date nDate = (Date) dateTime.clone();
        nDate.setSeconds(dateTime.getSeconds() - 30);
        dateTime.setSeconds(dateTime.getSeconds() + 30);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        Map<Long, Object> map = new HashMap<>();
        if (rows.size() > 0) {
            map = Averaging.getValues(rows, nDate, "minutes");
        }
        return new GraphPoints(rows.size(), new TreeMap<Long, Object>(map));
    }
}





//    private GraphPoints averaging(List<Map<String, Object>> rows) {
//        Map<Long, Double> map = new HashMap<>();
//        long date = 0;
//        double sumValues = 0, roundVar = 0;
//        int COUNT_POINTS = 20, countValues = 0, countPoint = 0, i = 1, rowSize = rows.size(), merger = (int) Math.floor(rowSize / (COUNT_POINTS));
//        if (rowSize > 0) {
//            if (rowSize <= COUNT_POINTS) {
//                for (i = 0; i < rowSize; i++) {
//                    map.put((long) (((java.sql.Timestamp) rows.get(i).get("date_time")).getTime()), (double) rows.get(i).get("value"));
//                }
//            } else {
//                map.put((long) (((java.sql.Timestamp) rows.get(0).get("date_time")).getTime()), (double) rows.get(0).get("value"));
//                while (countPoint != COUNT_POINTS) {
//                    while (merger != countValues) {
//                        if (i < rowSize) {
//                            sumValues += (double) rows.get(i).get("value");
//                            date = (long) (((java.sql.Timestamp) rows.get(i).get("date_time")).getTime());
//                            countValues++;
//                            i++;
//                        }
//                    }
//                    roundVar = new BigDecimal(sumValues / merger).setScale(3, RoundingMode.UP).doubleValue();
//                    map.put(date, roundVar);
//                    sumValues = 0;
//                    countValues = 0;
//                    countPoint++;
//
//                }
//                if (i != rowSize) {
//                    while (i != rowSize) {
//                        sumValues += (double) rows.get(i).get("value");
//                        date = (long) (((java.sql.Timestamp) rows.get(i).get("date_time")).getTime());
//                        countValues++;
//                        i++;
//                    }
//                    roundVar = new BigDecimal(sumValues / countValues).setScale(3, RoundingMode.UP).doubleValue();
//                    map.put(date, roundVar);
//                }
//            }
//        }
//        return new GraphPoints(rowSize, new TreeMap<Long, Object>(map));
//    }