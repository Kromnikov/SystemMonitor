package net.core;


import net.core.configurations.SSHConfiguration;
import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
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
@Service("MetricStorage")
public class MetricStorage implements IMetricStorage {

    private JdbcTemplate jdbcTemplateObject;

    @Autowired
    private HostService hosts;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public MetricStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }


    //sql
    //metric-state
    @Transactional
    public void setAllowableValueMetric(String endTime, int instMetric) {
        String sql = "UPDATE \"METRIC_STATE\" SET \"end_datetime\" = (TIMESTAMP '" + endTime + "')  where  inst_metric =" + instMetric + " and \"end_datetime\" is null";
        jdbcTemplateObject.update(sql);
    }

    @Transactional //MAX
    public boolean overMaxValue(long instMetric) {
        String sql = "SELECT id, state, start_datetime, \"end_datetime\", inst_metric,host_id  FROM \"METRIC_STATE\" where  inst_metric =" + instMetric + " and \"end_datetime\" is null and state like '%превысило%'";
        boolean state = true;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return state;
        } else {
            return false;
        }
    }

    @Transactional
    public void setOverMaxValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric) {
        String state = "'Значение " + valueMetric + " превысило пороговое значение " + instanceMetric.getMaxValue() + "'";
        String sql = "INSERT INTO \"METRIC_STATE\"(start_datetime,state,inst_metric,resolved,host_id) " +
                " VALUES ((TIMESTAMP '" + startTime + "')," + state + "," + instanceMetric.getId() + ",false," + hostId + ")";
        jdbcTemplateObject.update(sql);
    }


    @Transactional //MIN
    public boolean lessMinValue(long instMetric) {
        String sql = "SELECT id, state, start_datetime, \"end_datetime\", inst_metric  FROM \"METRIC_STATE\" where  inst_metric =" + instMetric + " and \"end_datetime\" is null and state like '%ниже%'";
        boolean state = true;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return state;
        } else {
            return false;
        }
    }

    @Transactional  //MIN
    public void setLessMinValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric) {
        String state = "'Значение " + valueMetric + " ниже порогового значения " + instanceMetric.getMinValue() + "'";
        String sql = "INSERT INTO \"METRIC_STATE\"(start_datetime,state,inst_metric,resolved,host_id) " +
                " VALUES ((TIMESTAMP '" + startTime + "')," + state + "," + instanceMetric.getId() + ",false," + hostId + ")";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public boolean correctlyMetric(long instMetric) {
        String sql = "SELECT id, state, start_datetime, \"end_datetime\", inst_metric  FROM \"METRIC_STATE\" where state='unknow' and inst_metric =" + instMetric + " and \"end_datetime\" is null";
        boolean state = true;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return state;
        } else {
            return false;
        }
    }

    @Transactional
    public void setCorrectlyMetric(String endTime, int instMetric) {
        String sql = "UPDATE \"METRIC_STATE\" SET \"end_datetime\" = (TIMESTAMP '" + endTime + "')  where state='unknow' and  inst_metric =" + instMetric + " and \"end_datetime\" is null";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public void setIncorrectlyMetric(String startTime, int instMetric) {
        String sql = "INSERT INTO \"METRIC_STATE\"(start_datetime,state,inst_metric,resolved)  VALUES ((TIMESTAMP '" + startTime + "'),'unknow'," + instMetric + ",false)";
        jdbcTemplateObject.update(sql);
    }


    @Transactional
    public TableModel getMetricTableModel() {
        TableModel metricsTableModel = new TableModel();
        String sql = "SELECT id, state, start_datetime, end_datetime, inst_metric, resolved  FROM \"METRIC_STATE\" where resolved = false";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return metricsTableModel;
        } else {
            String[] header = {"id", "Статус", "Дата начала", "Дата окончания", "Метрика", "Разрешено"};
            String[][] data = new String[rows.size()][6];
            int i = 0;
            for (Map row : rows) {
                data[i][0] = row.get("id").toString();
                data[i][1] = row.get("state").toString();
                data[i][2] = row.get("start_datetime").toString();
                if (row.get("end_datetime") != null) {
                    data[i][3] = row.get("end_datetime").toString();
                } else {
                    data[i][3] = " ";
                }
                data[i][4] = row.get("inst_metric").toString();
                data[i][5] = row.get("resolved").toString();
                i++;
            }
            metricsTableModel = new TableModel(header, data);
        }
        return metricsTableModel;
    }

    @Transactional
    public List<MetricState> getMetricProblems(int hostId) throws SQLException, ParseException {
        List<MetricState> metricStateList = new ArrayList<>();
        String sql = "SELECT id, state, start_datetime, end_datetime, inst_metric, resolved FROM \"METRIC_STATE\" where resolved = false and host_id=" + hostId;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return metricStateList;
        } else {
            int i = 0;
            for (Map row : rows) {
                MetricState metricStateTmp = new MetricState();
                metricStateTmp.setId(Integer.parseInt(row.get("id").toString()));
                metricStateTmp.setValue((row.get("state").toString()));
                metricStateTmp.setStart(dateFormat.parse(row.get("start_datetime").toString()));
                if (row.get("end_datetime") != null) {
                    metricStateTmp.setEnd(dateFormat.parse(row.get("end_datetime").toString()));
                } else {
                }
                metricStateTmp.setInstMetric(getInstMetric(Integer.parseInt(row.get("inst_metric").toString())).getTitle());
                metricStateTmp.setResolved(Boolean.parseBoolean(row.get("resolved").toString()));
                i++;
                metricStateList.add(metricStateTmp);
            }
//            metricsTableModel = new TableModel(header,data);
        }
        return metricStateList;
    }
    @Transactional
    public List<MetricState> getMetricProblems(int hostId,int metricId) throws SQLException, ParseException {
        List<MetricState> metricStateList = new ArrayList<>();
        String sql = "SELECT id, state, start_datetime, end_datetime, inst_metric, resolved FROM \"METRIC_STATE\" where resolved = false and inst_metric=" + metricId;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return metricStateList;
        } else {
            int i = 0;
            for (Map row : rows) {
                MetricState metricStateTmp = new MetricState();
                metricStateTmp.setId(Integer.parseInt(row.get("id").toString()));
                metricStateTmp.setValue((row.get("state").toString()));
                metricStateTmp.setStart(dateFormat.parse(row.get("start_datetime").toString()));
                if (row.get("end_datetime") != null) {
                    metricStateTmp.setEnd(dateFormat.parse(row.get("end_datetime").toString()));
                } else {
                }
                metricStateTmp.setInstMetric(getInstMetric(Integer.parseInt(row.get("inst_metric").toString())).getTitle());
                metricStateTmp.setResolved(Boolean.parseBoolean(row.get("resolved").toString()));
                i++;
                metricStateList.add(metricStateTmp);
            }
//            metricsTableModel = new TableModel(header,data);
        }
        return metricStateList;
    }

    @Transactional
    public List<MetricState> getMetricProblems() throws SQLException, ParseException {
        List<MetricState> metricStateList = new ArrayList<>();
        String sql = "SELECT id, state, start_datetime, end_datetime, inst_metric, resolved  FROM \"METRIC_STATE\" where resolved = false";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return metricStateList;
        } else {
            for (Map row : rows) {
                MetricState metricStateTmp = new MetricState();
                metricStateTmp.setId(Integer.parseInt(row.get("id").toString()));
                metricStateTmp.setValue((row.get("state").toString()));
                metricStateTmp.setStart(dateFormat.parse(row.get("start_datetime").toString()));
                if (row.get("end_datetime") != null) {
                    metricStateTmp.setEnd(dateFormat.parse(row.get("end_datetime").toString()));
                } else {
                }
                metricStateTmp.setInstMetric(getInstMetric(Integer.parseInt(row.get("inst_metric").toString())).getTitle());
                metricStateTmp.setResolved(Boolean.parseBoolean(row.get("resolved").toString()));
                metricStateList.add(metricStateTmp);
            }
        }
        return metricStateList;
    }

    @Transactional
    public void setResolvedMetric(int id) {
        String sql = "UPDATE \"METRIC_STATE\" set resolved = true WHERE id =" + id + " and \"end_datetime\" is not null";
        jdbcTemplateObject.update(sql);
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
    public long getMetricNotResolvedLength(int hostId) throws SQLException {
        String sql = "SELECT COUNT(*)  FROM \"METRIC_STATE\" where resolved = false and host_id =" + hostId;
        return (long) jdbcTemplateObject.queryForMap(sql).get("COUNT");
    }

    @Transactional
    public int getProblemsLength() {
        return (int) (getHostNotResolvedLength() + getMetricNotResolvedLength());
    }


    //host-state
    @Transactional
    public boolean availableHost(long hostId) {//Нужен запрос на вывод состояния хоста
        String sql = "SELECT id, resolved, start_datetime, \"end_datetime\", host  FROM \"HOST_STATE\" where host = " + hostId + " and \"end_datetime\" is null";
        boolean state = true;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
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
    public void setNotAvailableHost(String startTime, int host, String hostName) {
        String sql = "INSERT INTO \"HOST_STATE\"(start_datetime,resolved,host,host_name)  VALUES ((TIMESTAMP '" + startTime + "'),false," + host + ",'" + hostName + "')";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public void setAvailableHost(String endTime, int host) {
        String sql = "UPDATE \"HOST_STATE\" SET \"end_datetime\" = (TIMESTAMP '" + endTime + "')  where host =" + host + " and \"end_datetime\" is null";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public TableModel getHostTableModel() {
        TableModel metricsTableModel = new TableModel();
        String sql = "SELECT id, resolved, start_datetime, end_datetime, host  FROM \"HOST_STATE\" where resolved = false";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return metricsTableModel;
        } else {
            String[] header = {"id", "Разрешено", "Дата начала", "Дата окончания", "Хост"};
            String[][] data = new String[rows.size()][5];
            int i = 0;
            for (Map row : rows) {
                data[i][0] = row.get("id").toString();
                data[i][1] = row.get("resolved").toString();
                data[i][2] = row.get("start_datetime").toString();
                if (row.get("end_datetime") != null) {
                    data[i][3] = row.get("end_datetime").toString();
                } else {
                    data[i][3] = " ";
                }
                data[i][4] = row.get("host").toString();
                i++;
            }
            metricsTableModel = new TableModel(header, data);
        }
        return metricsTableModel;
    }

    @Transactional
    public List<HostsState> getHostsProblems() throws SQLException, ParseException {
        List<HostsState> hostsStateList = new ArrayList<>();
        String sql = "SELECT id, resolved, start_datetime, end_datetime, host,host_name  FROM \"HOST_STATE\" where resolved = false";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return hostsStateList;
        } else {
            int i = 0;
            for (Map row : rows) {
                HostsState hostStateTmp = new HostsState();
                hostStateTmp.setId(Integer.parseInt(row.get("id").toString()));
//                hostStateTmp.setState(row.get("state").toString());
                hostStateTmp.setStart(dateFormat.parse(row.get("start_datetime").toString()));
                if (row.get("end_datetime") != null) {
                    hostStateTmp.setEnd(dateFormat.parse(row.get("end_datetime").toString()));
                } else {
//                    data[i][3] = " ";
                }
//                hostStateTmp.setInstMetric(Integer.parseInt(row.get("inst_metric").toString()));
                hostStateTmp.setResolved(Boolean.parseBoolean(row.get("resolved").toString()));
                hostStateTmp.setHostId(Integer.parseInt(row.get("host").toString()));

                hostStateTmp.setHostName(hosts.get(hostStateTmp.getHostId()).getName());
                i++;
                hostsStateList.add(hostStateTmp);
            }
//            metricsTableModel = new TableModel(header,data);
        }
        return hostsStateList;
    }

    @Transactional
    public void setResolvedHost(int id) {
        String sql = "UPDATE \"HOST_STATE\" set resolved = true WHERE id =" + id + " and \"end_datetime\" is not null";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public void setResolvedHost() {
        String sql = "UPDATE \"HOST_STATE\" set resolved = true WHERE \"end_datetime\" is not null";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public long getHostNotResolvedLength() {
        String sql = "SELECT COUNT(*)  FROM \"HOST_STATE\" where resolved = false";
        return (long) jdbcTemplateObject.queryForMap(sql).get("COUNT");
    }


    //values
    @Transactional
    public void addValue(int host, int metric, double value, String dateTime) throws SQLException {
        String sql = "INSERT INTO \"VALUE_METRIC\"(host, metric, value,date_time)  VALUES (" + host + "," + metric + "," + value + ",(TIMESTAMP '" + dateTime + "'))";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public Date getLastDate(int hostId, int metricId) {
        String sql = "SELECT MAX(date_time) FROM \"VALUE_METRIC\" where metric = " + metricId + " and host =" + hostId;
        return (Date) jdbcTemplateObject.queryForMap(sql).get("MAX");
    }

    private chartValues averaging(List<Map<String, Object>> rows) {
        Map<Long, Double> map = new HashMap<>();
        long date = 0;
        double sumValues = 0, roundVar = 0;
        int COUNT_POINTS = 20, countValues = 0, countPoint = 0, i = 1, rowSize = rows.size(), merger = (int) Math.floor(rowSize / (COUNT_POINTS));
        if (rowSize > 0) {
            if (rowSize <= COUNT_POINTS) {
                for (i = 0; i < rowSize; i++) {
                    map.put((long) (((java.sql.Timestamp) rows.get(i).get("date_time")).getTime()), (double) rows.get(i).get("value"));
                }
            } else {
                map.put((long) (((java.sql.Timestamp) rows.get(0).get("date_time")).getTime()), (double) rows.get(0).get("value"));
                while (countPoint != COUNT_POINTS) {
                    while (merger != countValues) {
                        if (i < rowSize) {
                            sumValues += (double) rows.get(i).get("value");
                            date = (long) (((java.sql.Timestamp) rows.get(i).get("date_time")).getTime());
                            countValues++;
                            i++;
                        }
                    }
                    roundVar = new BigDecimal(sumValues / merger).setScale(3, RoundingMode.UP).doubleValue();
                    map.put(date, roundVar);
                    sumValues = 0;
                    countValues = 0;
                    countPoint++;

                }
                if (i != rowSize) {
                    while (i != rowSize) {
                        sumValues += (double) rows.get(i).get("value");
                        date = (long) (((java.sql.Timestamp) rows.get(i).get("date_time")).getTime());
                        countValues++;
                        i++;
                    }
                    roundVar = new BigDecimal(sumValues / countValues).setScale(3, RoundingMode.UP).doubleValue();
                    map.put(date, roundVar);
                }
            }
        }
        return new chartValues(rowSize, new TreeMap<Long, Double>(map));
    }

    @Transactional
    public chartValues getAllValues(int host_id, int metricId) {
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where metric = " + metricId + " and host = " + host_id + " order by date_time ";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        Map<Long, Double> map = new HashMap<>();
        if (rows.size() > 0) {
                for (int i = 0; i < rows.size(); i++) {
                    map.put((long) (((java.sql.Timestamp) rows.get(i).get("date_time")).getTime()), (double) rows.get(i).get("value"));
                }
        }
        return new chartValues(rows.size(), new TreeMap<Long, Double>(map));
//        return averaging(jdbcTemplateObject.queryForList(sql));
    }

    @Transactional
    public chartValues getValuesLastDay(int host_id, int metricId, Date dateTime) {
        Date nDate = (Date) dateTime.clone();
        nDate.setHours(dateTime.getHours() - 24);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";
//        return averaging(jdbcTemplateObject.queryForList(sql));


        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        double values = (double) rows.get(0).get("value");
        Map<Long, Double> map = new HashMap<>();
        nDate = ((java.sql.Timestamp) rows.get(0).get("date_time"));
        nDate.setSeconds(0);
        nDate.setMinutes(0);
//        System.out.println(nDate);
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

        return new chartValues(rowSize, new TreeMap<Long, Double>(map));
    }

    @Transactional
    public chartValues getValuesDay(int host_id, int metricId,  Date dateTime) {
        Date nDate = (Date) dateTime.clone();
        nDate.setHours(dateTime.getHours() - 24);
        dateTime.setHours(dateTime.getHours() + 24);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";
//        return averaging(jdbcTemplateObject.queryForList(sql));


        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        double values = (double) rows.get(0).get("value");
        Map<Long, Double> map = new HashMap<>();
        nDate = ((java.sql.Timestamp) rows.get(0).get("date_time"));
        nDate.setSeconds(0);
        nDate.setMinutes(0);
//        System.out.println(nDate);
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

        return new chartValues(rowSize, new TreeMap<Long, Double>(map));
    }

    @Transactional
    public chartValues getValuesTheeDays(int host_id, int metricId,  Date dateTime) {
        Date nDate = (Date) dateTime.clone();
        nDate.setHours(dateTime.getHours() - 60);
        dateTime.setHours(dateTime.getHours() + 60);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";
//        return averaging(jdbcTemplateObject.queryForList(sql));

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        double values = (double) rows.get(0).get("value");
        Map<Long, Double> map = new HashMap<>();
        nDate = ((java.sql.Timestamp) rows.get(0).get("date_time"));
        nDate.setSeconds(0);
        nDate.setMinutes(0);
//        System.out.println(nDate);
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

        return new chartValues(rowSize, new TreeMap<Long, Double>(map));
    }

    @Transactional
    public chartValues getValuesMonth(int host_id, int metricId,  Date dateTime) {
        Date nDate = (Date) dateTime.clone();
        nDate.setHours(dateTime.getHours() - 360);
        dateTime.setHours(dateTime.getHours() + 360);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";

//        return averaging(jdbcTemplateObject.queryForList(sql));



        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        double values = (double) rows.get(0).get("value");
        Map<Long, Double> map = new HashMap<>();
        nDate = ((java.sql.Timestamp) rows.get(0).get("date_time"));
        nDate.setSeconds(0);
        nDate.setMinutes(0);
        nDate.setHours(0);
//        System.out.println(nDate);
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

        return new chartValues(rowSize, new TreeMap<Long, Double>(map));
    }

    @Transactional
    public chartValues getValuesSixMonth(int host_id, int metricId,  Date dateTime) {
        Date nDate = (Date) dateTime.clone();
        nDate.setMonth(dateTime.getMonth() - 3);
        dateTime.setMonth(dateTime.getMonth() + 3);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";

//        return averaging(jdbcTemplateObject.queryForList(sql));

        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        double values = (double) rows.get(0).get("value");
        Map<Long, Double> map = new HashMap<>();
        nDate = ((java.sql.Timestamp) rows.get(0).get("date_time"));
        nDate.setSeconds(0);
        nDate.setMinutes(0);
        nDate.setHours(0);
//        System.out.println(nDate);
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

        return new chartValues(rowSize, new TreeMap<Long, Double>(map));
    }

    @Transactional
    public chartValues getValuesYear(int host_id, int metricId,  Date dateTime) {
        Date nDate = (Date) dateTime.clone();
        nDate.setMonth(dateTime.getMonth() - 6);
        dateTime.setMonth(dateTime.getMonth() + 6);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";

//        return averaging(jdbcTemplateObject.queryForList(sql));


        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        double values = (double) rows.get(0).get("value");
        Map<Long, Double> map = new HashMap<>();
        nDate = ((java.sql.Timestamp) rows.get(0).get("date_time"));
        nDate.setSeconds(0);
        nDate.setMinutes(0);
        nDate.setHours(0);
//        nDate.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(nDate);
//        calendar.set(Calendar.MILLISECOND, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.HOUR, 0);
//        System.out.println(nDate);
//        nDate= calendar.getTime();
//        System.out.println(nDate);

        for (int i = 1; i < rowSize; i++) {
            if ((nDate.getYear() == ((java.sql.Timestamp) rows.get(i).get("date_time")).getYear()) & (nDate.getMonth() == ((java.sql.Timestamp) rows.get(i).get("date_time")).getMonth())) {
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

        return new chartValues(rowSize, new TreeMap<Long, Double>(map));
    }

    @Transactional
    public chartValues getValuesLastHour(int host_id, int metricId,  Date dateTime) {
        Date nDate = (Date) dateTime.clone();
        nDate.setMinutes(dateTime.getMinutes() - 30);
        dateTime.setMinutes(dateTime.getMinutes() + 30);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";
//        return averaging(jdbcTemplateObject.queryForList(sql));


        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        Map<Long, Double> map = new HashMap<>();
        if (rowSize > 0) {
            double values = (double) rows.get(0).get("value");
            nDate = ((java.sql.Timestamp) rows.get(0).get("date_time"));
            nDate.setSeconds(0);
            for (int i = 1; i < rowSize; i++) {
                if (nDate.getMinutes() == ((java.sql.Timestamp) rows.get(i).get("date_time")).getMinutes()) {
                    values += (double) rows.get(i).get("value");
                    counter++;
                } else {
                    map.put((long) nDate.getTime(), values / counter);
                    nDate = ((java.sql.Timestamp) rows.get(i).get("date_time"));
                    nDate.setSeconds(0);
                    values = (double) rows.get(i).get("value");
                    counter = 1;
                }
                map.put((long) nDate.getTime(), values / counter);
            }
        }
        return new chartValues(rowSize, new TreeMap<Long, Double>(map));
    }

    @Transactional
    public chartValues getValuesTheeMinutes(int host_id, int metricId,  Date dateTime) {
        Map<Long, Double> map = new HashMap<>();
        Date nDate = (Date) dateTime.clone();
        nDate.setMinutes(dateTime.getMinutes() - 1);
        dateTime.setMinutes(dateTime.getMinutes() + 1);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        int rowSize = rows.size();
        if (rowSize > 0) {
            for (int i = 0; i < rowSize; i++) {
                map.put((long) (((java.sql.Timestamp) rows.get(i).get("date_time")).getTime()), (double) rows.get(i).get("value"));
            }
        }
        return new chartValues(rowSize, new TreeMap<Long, Double>(map));
    }

    @Transactional
    public chartValues getValuesOneMinutes(int host_id, int metricId,  Date dateTime) {
        Map<Long, Double> map = new HashMap<>();
        Date nDate = (Date) dateTime.clone();
        nDate.setSeconds(dateTime.getSeconds() - 30);
        dateTime.setSeconds(dateTime.getSeconds() + 30);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        int rowSize = rows.size();
        if (rowSize > 0) {
            for (int i = 0; i < rowSize; i++) {
                map.put((long) (((java.sql.Timestamp) rows.get(i).get("date_time")).getTime()), (double) rows.get(i).get("value"));
            }
        }
        return new chartValues(rowSize, new TreeMap<Long, Double>(map));
    }


    @Transactional
    public chartValues getValuesByZoom(int host_id, int metricId, int zoom) {
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time < '" + getLastDate(host_id, metricId) + "'  and  metric = " + metricId + " and host = " + host_id + " order by date_time DESC limit " + zoom;
        return averaging(jdbcTemplateObject.queryForList(sql));
    }

//    @Transactional
//    public chartValues getValuesByZoom(int host_id, int metricId,  Date dateTime) {
//        String sql = "(SELECT value,date_time FROM \"VALUE_METRIC\" where date_time < '" + dateFormat.format(dateTime) + "'  and metric = " + metricId + " and host = " + host_id + " order by date_time  limit " + limit + " )" +
//                "UNION ALL\n" +
//                "(SELECT value,date_time FROM \"VALUE_METRIC\"  where date_time >= '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time   limit " + limit + " )";
//
//        return averaging(jdbcTemplateObject.queryForList(sql));
//    }


    //metrics
    @Transactional
    public void addTemplateMetric(String title, String query) throws SQLException {
        String sql = "INSERT INTO \"TEMPLATE_METRICS\"(title, query) VALUES (" + title + "," + query + ")";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public TemplateMetric getTemplateMetric(int id) throws SQLException {
        TemplateMetric templateMetric = new TemplateMetric();
        String sql = "select * FROM \"TEMPLATE_METRICS\" where id =" + id;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
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
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
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
        String sql = "select id FROM \"TEMPLATE_METRICS\" where title='" + title + "'";
        return (int) jdbcTemplateObject.queryForMap(sql).get("id");
    }

    @Transactional
    public TemplateMetric getTemplatMetric(String title) throws SQLException {
        TemplateMetric templateMetric = new TemplateMetric();
        String sql = "select * FROM \"TEMPLATE_METRICS\" where title ='" + title + "'";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
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
        String sql = "INSERT INTO \"INSTANCE_METRIC\"(host, templ_metric,min_value,max_value,title,query) VALUES (" + host + "," + metric + ",0,0,'" + templateMetric.getTitle() + "',$q$" + templateMetric.getCommand() + "$q$)";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public void addInstMetric(SSHConfiguration host, TemplateMetric templateMetric) throws SQLException {
        String sql = "INSERT INTO \"INSTANCE_METRIC\"(host, templ_metric,min_value,max_value,title,query) VALUES (" + host.getId() + "," + templateMetric.getId() + ",0,0,'" + templateMetric.getTitle() + "',$q$" + templateMetric.getCommand() + "$q$)";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public void addInstMetric(InstanceMetric instanceMetric) throws SQLException {
        String sql = "INSERT INTO \"INSTANCE_METRIC\"(host, templ_metric,min_value,max_value,title,query) VALUES (" + instanceMetric.getHostId() + "," + instanceMetric.getTempMetrcId() + "," + instanceMetric.getMinValue() + "," + instanceMetric.getMaxValue() + ",'" + instanceMetric.getTitle() + "',$q$" + instanceMetric.getCommand() + "$q$)";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public List<InstanceMetric> getInstMetrics(int hostId) throws SQLException {
        List<InstanceMetric> instanceMetrics = new ArrayList<>();
        String sql = "SELECT id, templ_metric, title, query, min_value, max_value, host  FROM \"INSTANCE_METRIC\" where host =" + hostId;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
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
    public InstanceMetric getInstMetric(int hostId, String title) throws SQLException {
        InstanceMetric instanceMetric = new InstanceMetric();
        String sql = "SELECT id, templ_metric, title, query, min_value, max_value, host  FROM \"INSTANCE_METRIC\" where host =" + hostId + " and title = '" + title + "'";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            instanceMetric.setId((int) row.get("id"));
            instanceMetric.setHostId(hostId);
            instanceMetric.setTempMetrcId((int) row.get("templ_metric"));
            instanceMetric.setMinValue((double) row.get("min_value"));
            instanceMetric.setMaxValue((double) row.get("max_value"));
            instanceMetric.setCommand((String) row.get("query"));
            instanceMetric.setTitle((String) row.get("title"));
        }
        return instanceMetric;
    }

    @Transactional
    public InstanceMetric getInstMetric(int instMetricId) throws SQLException {
        InstanceMetric instanceMetric = new InstanceMetric();
        String sql = "SELECT id, templ_metric, title, query, min_value, max_value, host  FROM \"INSTANCE_METRIC\" where id =" + instMetricId;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            instanceMetric.setId((int) row.get("id"));
            instanceMetric.setHostId((int) row.get("host"));
            instanceMetric.setTempMetrcId((int) row.get("templ_metric"));
            instanceMetric.setMinValue((double) row.get("min_value"));
            instanceMetric.setMaxValue((double) row.get("max_value"));
            instanceMetric.setCommand((String) row.get("query"));
            instanceMetric.setTitle((String) row.get("title"));
        }
        return instanceMetric;
    }

    @Transactional
    public void delInstMetric(int metricId) throws SQLException {
        String sql = "DELETE FROM \"INSTANCE_METRIC\" WHERE id=" + metricId;
        jdbcTemplateObject.update(sql);
    }

    //hostsRows
    @Transactional
    public List<hostRow> getHostRow() throws SQLException {
        List<hostRow> hostrows = new ArrayList<>();
        String sql = "(select count(*) as countServices,host,(select count(*)from \"METRIC_STATE\" where host_id = im.host) as countProblems ,(select count(*)from \"HOST_STATE\" where host = im.host and (\"end_datetime\" is null and \"start_datetime\" is not null)) as status from \"INSTANCE_METRIC\" as im group by host)";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        for (SSHConfiguration host : this.hosts.getAll()) {
            hostRow hostRow = new hostRow();
            hostRow.setId(host.getId());
            hostRow.setHostName(host.getName());
            for (Map row : rows) {
                if(host.getId()==(int) row.get("host")) {
                    hostRow.setServicesCount(Integer.parseInt(row.get("countServices").toString()));
                    hostRow.setErrorsCount(Integer.parseInt(row.get("countProblems").toString()));
                    hostRow.setStatus(row.get("status").toString());
                }
            }
            hostrows.add(hostRow);
        }
        return hostrows;
    }
    //metricRows
    @Transactional
    public List<metricRow> getMetricRow(int hostId) throws SQLException {
        List<metricRow> metricRows = new ArrayList<>();
        String sql = "select id,title ,(select value from \"VALUE_METRIC\" where metric = im.id ORDER BY id DESC limit 1) as value ,(select date_time from \"VALUE_METRIC\" where metric = im.id ORDER BY id DESC limit 1) as date ,(select count(*)from \"METRIC_STATE\" where inst_metric = im.id ) as countProblems ,(select count(*)from \"METRIC_STATE\" where inst_metric = im.id and (\"end_datetime\" is null and \"start_datetime\" is not null)) as status  from \"INSTANCE_METRIC\" as im where host = " + hostId;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            metricRow metricrow = new metricRow();
            metricrow.setId(Integer.parseInt(row.get("id").toString()));
            metricrow.setTitle((row.get("title").toString()));
            metricrow.setErrorsCount(Integer.parseInt(row.get("countProblems").toString()));
            metricrow.setLastValue(Double.parseDouble(row.get("value").toString()));
            metricrow.setDate(((java.sql.Timestamp) row.get("date")));
            metricrow.setStatus(row.get("status").toString());
            metricRows.add(metricrow);
        }
        return metricRows;
    }
    @Transactional
    public List<metricRow> getMetricRow(int hostId,int metricId) throws SQLException {
        List<metricRow> metricRows = new ArrayList<>();
        String sql = "select id,title ,(select value from \"VALUE_METRIC\" where metric = im.id ORDER BY id DESC limit 1) as value ,(select date_time from \"VALUE_METRIC\" where metric = im.id ORDER BY id DESC limit 1) as date ,(select count(*)from \"METRIC_STATE\" where inst_metric = im.id ) as countProblems ,(select count(*)from \"METRIC_STATE\" where inst_metric = im.id and (\"end_datetime\" is null and \"start_datetime\" is not null)) as status  from \"INSTANCE_METRIC\" as im where id = " + metricId;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            metricRow metricrow = new metricRow();
            metricrow.setId(Integer.parseInt(row.get("id").toString()));
            metricrow.setTitle((row.get("title").toString()));
            metricrow.setErrorsCount(Integer.parseInt(row.get("countProblems").toString()));
            metricrow.setLastValue(Double.parseDouble(row.get("value").toString()));
            metricrow.setDate(((java.sql.Timestamp) row.get("date")));
            metricrow.setStatus(row.get("status").toString());
            metricRows.add(metricrow);
        }
        return metricRows;
    }
    //Favorites
    @Transactional
    public List<Favorites> getFavoritesRow() throws SQLException {
        List<Favorites> favoriteses = new ArrayList<>();
        String sql = "select *,(select title from \"INSTANCE_METRIC\" where id = f.inst_metric_id) as title  from \"FAVORITES\" as f";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

            for (Map row : rows) {
                Favorites favorite = new Favorites();
                favorite.setId((int) row.get("id"));
                favorite.setHostId(Integer.parseInt(row.get("host_id").toString()));
                favorite.setMetricId(Integer.parseInt(row.get("inst_metric_id").toString()));
                favorite.setTitle(row.get("title").toString());
                favoriteses.add(favorite);
            }
        return favoriteses;
    }

    //problem
    @Transactional
    public Problem getProblem(int problemId) throws SQLException {
        Problem problem = new Problem();
        getInstMetric(problemId);
        String sql = "SELECT i.title , a.host_id, a.inst_metric FROM \"METRIC_STATE\" as a , \"INSTANCE_METRIC\" as i where a.id = " + problemId + " and i.id = a.inst_metric";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            problem.setHostId((int) row.get("host_id"));
            problem.setInstMetricId((int) row.get("inst_metric"));
            problem.setInstMetric((String) row.get("title"));
        }
        return problem;
    }

    @Transactional
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        String sql = "SELECT u.id, u.username , u.password, r.role FROM \"Users\" as u, \"Roles\" as r where u.id=r.id";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        for (Map row : rows) {
            User user = new User();
            user.setId((int) row.get("id"));
            user.setUsername((String) row.get("username"));
            user.setPassword((String) row.get("password"));
            user.setRole((String) row.get("role"));
            usersList.add(user);
        }
        return usersList;
    }


    @Transactional
    public long getQuantityOfRow(int id) throws SQLException {
        String sql = "select count(*) from \"VALUE_METRIC\" where metric =" + id;
        return (long) jdbcTemplateObject.queryForMap(sql).get("count");
    }

    @Override
    public void delMetricFromHost(int id) throws SQLException {

    }

    @Transactional
    public int getHostIDbyTitle(String title) throws SQLException {
        String sql = "select sshconfigurationhibernate_id from \"sshconfigurationhibernate\" where host='" + title + "'";
        return (int) jdbcTemplateObject.queryForMap(sql).get("sshconfigurationhibernate_id");
    }

    @Transactional
    public void addStandartMetrics(int id) throws SQLException {
        String sql = "INSERT INTO \"INSTANCE_METRIC\" (TEMPL_METRIC,HOST) VALUES (1," + id + ");";
        jdbcTemplateObject.update(sql);
        String sql1 = "INSERT INTO \"INSTANCE_METRIC\" (TEMPL_METRIC,HOST) VALUES (2," + id + ");";
        jdbcTemplateObject.update(sql1);
        String sql2 = "INSERT INTO \"INSTANCE_METRIC\" (TEMPL_METRIC,HOST) VALUES (5," + id + ");";
        jdbcTemplateObject.update(sql2);
    }


    @Transactional
    public void delMetricFromHost(int host, int id) throws SQLException {
        String sql = "delete from  \"HOST_METRIC\" where metric_id=" + id + " and host_id=" + host;
        jdbcTemplateObject.update(sql);
    }

    //TODO Favorites
    @Transactional
    public void addToFavorites(int host, int metric) throws SQLException {
        String sql = "INSERT INTO \"FAVORITES\"(host_id,inst_metric_id) VALUES (" + host + "," + metric + ")";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public void dellFromFavorites(int favoritesId) throws SQLException {
        String sql = "DELETE FROM \"FAVORITES\" WHERE id = "+favoritesId;
        jdbcTemplateObject.update(sql);
    }

    //TODO problems count home page
    @Transactional
    public int hostsProblemsCount() throws SQLException {
        String sql = "SELECT count(*)  FROM \"HOST_STATE\"  where \"end_datetime\" is null";
        return Integer.parseInt(jdbcTemplateObject.queryForMap(sql).get("count").toString());
    }
    @Transactional
    public int hostsSuccesCount() throws SQLException {
        String sql = "SELECT count(*)  FROM \"HOST_STATE\"  where \"end_datetime\" is not null";
        return Integer.parseInt(jdbcTemplateObject.queryForMap(sql).get("count").toString());
    }
    @Transactional
    public int metricsProblemCount() throws SQLException {
        String sql = "SELECT count(*)  FROM \"METRIC_STATE\"  where \"end_datetime\" is null";
        return Integer.parseInt(jdbcTemplateObject.queryForMap(sql).get("count").toString());
    }
    @Transactional
    public int metricsSuccesCount() throws SQLException {
        String sql = "SELECT count(*)  FROM \"METRIC_STATE\"  where \"end_datetime\" is not null";
        return Integer.parseInt(jdbcTemplateObject.queryForMap(sql).get("count").toString());
    }
}
