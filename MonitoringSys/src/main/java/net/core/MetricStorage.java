package net.core;


import net.core.alarms.GenericAlarm;
import net.core.alarms.dao.UINotificationDao;
import net.core.alarms.dao.GenericAlarmDao;
import net.core.configurations.SSHConfiguration;
import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.*;
import net.core.tools.Averaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
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
    private UINotificationDao UINotificationDao;

    @Autowired
    private HostService hosts;

    @Autowired
    private GenericAlarmDao genericAlarm;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public MetricStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    //TODO:Развертываение
    @PostConstruct
    public void dump() throws IOException {
//        File f=new File("dump.sql");
//        try (FileReader reader  = new FileReader(f))
//        {
//            char[] buffer = new char[(int)f.length()];
//            reader.read(buffer);
////            System.out.println(new String(buffer));
//            jdbcTemplateObject.update(new String(buffer));
//        }
    }

    //TODO: alarms
    @Transactional
    public List<GenericAlarmsRow> getAlarms(String userName) {
        List<GenericAlarmsRow> genericAlarmsRowList = new ArrayList<>();
        String sql = "";
        List<Map<String, Object>> rows ;
        for (GenericAlarm ga : genericAlarm.getByUser(userName)) {
            GenericAlarmsRow genericAlarmsRow = new GenericAlarmsRow();
            genericAlarmsRow.setType(ga.getType());
            genericAlarmsRow.setMessage(ga.getMessage());
            if(ga.getHostId()>-1) {
                genericAlarmsRow.setHostId(ga.getHostId());
                genericAlarmsRow.setHostName(hosts.get(genericAlarmsRow.getHostId()).getName());
            }
            genericAlarmsRow.setId(ga.getId());
            {
                if (ga.getServiceId() > -1);
            }
            genericAlarmsRow.setServiceId(ga.getServiceId());
            genericAlarmsRow.setToEmail(ga.getToEmail());
            genericAlarmsRow.setToUser(ga.getToUser());
            genericAlarmsRow.setUser(ga.getUsername());


            sql = "select title,host from \"INSTANCE_METRIC\" WHERE id="+ genericAlarmsRow.getServiceId();
            rows= jdbcTemplateObject.queryForList(sql);
            for (Map row : rows) {
                genericAlarmsRow.setServiceTitle((String) row.get("title"));
                genericAlarmsRow.setFromHost(hosts.get((int) row.get("host")).getName());
            }


            genericAlarmsRowList.add(genericAlarmsRow);
        }
        return genericAlarmsRowList;
    }
    @Transactional
    public AlarmRow getAlarm(int id) throws SQLException {
        String sql = "";
        List<Map<String, Object>> rows;
        GenericAlarm ga = genericAlarm.get(id);

        SSHConfiguration host = new SSHConfiguration();
        AlarmRow alarmRow = new AlarmRow();
        alarmRow.setType(ga.getType());
        alarmRow.setMessage(ga.getMessage());
        if (ga.getHostId() > -1) {
            alarmRow.setHostId(ga.getHostId());
            host = hosts.get(alarmRow.getHostId());
            alarmRow.setHostName(host.getName());
        }
        alarmRow.setId(ga.getId());
        {
            if (ga.getServiceId() > -1) ;
        }
        alarmRow.setServiceId(ga.getServiceId());
        alarmRow.setToEmail(ga.getToEmail());
        alarmRow.setToUser(ga.getToUser());
        alarmRow.setUser(ga.getUsername());
        sql = "select title,host from \"INSTANCE_METRIC\" WHERE id=" + alarmRow.getServiceId();
        rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            alarmRow.setServiceTitle((String) row.get("title"));
            alarmRow.setFromHost(hosts.get((int) row.get("host")).getName());
        }

        alarmRow.setInstanceMetrics(getInstMetrics(host.getId()));
        alarmRow.setHosts(hosts.getAll());


        sql = "SELECT username FROM \"Users\"";
        rows = jdbcTemplateObject.queryForList(sql);
        List<String> stringList = new ArrayList<>();
        for (Map row : rows) {
            stringList.add((String)row.get("username"));
        }
        alarmRow.setAllUsers(stringList);

        return alarmRow;
    }
    @Transactional
    public AlarmRow getNewAlarm() throws SQLException {
        String sql = "";
        List<Map<String, Object>> rows;

        SSHConfiguration host = new SSHConfiguration();
        AlarmRow alarmRow = new AlarmRow();
        sql = "select title,host from \"INSTANCE_METRIC\" WHERE id=" + alarmRow.getServiceId();
        rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            alarmRow.setServiceTitle((String) row.get("title"));
            alarmRow.setFromHost(hosts.get((int) row.get("host")).getName());
        }

        alarmRow.setInstanceMetrics(getInstMetrics(host.getId()));
        alarmRow.setHosts(hosts.getAll());


        sql = "SELECT username FROM \"Users\"";
        rows = jdbcTemplateObject.queryForList(sql);
        List<String> stringList = new ArrayList<>();
        for (Map row : rows) {
            stringList.add((String)row.get("username"));
        }
        alarmRow.setAllUsers(stringList);

        return alarmRow;
    }

    @Transactional
    public void updateAlarm(int id, int serviseId, int hostId, String toEmail, String toUser) {
        String sql = "UPDATE genericalarm SET serviceid="+serviseId+", hostid="+hostId+", toemail='"+toEmail+"', touser='"+toUser+"' WHERE id="+id;
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public void addAlarm(int serviseId, int hostId, String toEmail, String toUser,String user) {
        String sql = "INSERT INTO genericalarm (serviceid, hostid, toemail, touser,  username)    VALUES ( '"+serviseId+"', '"+hostId+"', '"+toEmail+"', '"+toUser+"', '"+user+"')";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public void dellAlarm(int id) {
        String sql = "DELETE FROM genericalarm WHERE id="+id;
        jdbcTemplateObject.update(sql);
    }






//TODO: instMetric

    @Transactional
    public void addInstMetric(InstanceMetric instanceMetric) throws SQLException {
        String sql = "INSERT INTO \"INSTANCE_METRIC\"(host, templ_metric,min_value,max_value,title,query) VALUES (" + instanceMetric.getHostId() + "," + instanceMetric.getTempMetrcId() + "," + instanceMetric.getMinValue() + "," + instanceMetric.getMaxValue() + ",'" + instanceMetric.getTitle() + "',$q$" + instanceMetric.getCommand() + "$q$)";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public void editInstMetric(int id,int hostId,int templMetricId,String title,String command,double minValue,double maxValue) throws SQLException {
        String sql = "UPDATE \"INSTANCE_METRIC\" SET min_value='"+minValue+"', host='"+hostId+"', templ_metric='"+templMetricId+"',max_value='"+maxValue+"',title='"+title+"',query=$q$"+command+"$q$ WHERE id="+id;
        jdbcTemplateObject.update(sql);
    }










    //sql
    //metric-state
    @Transactional //MAX
    public boolean isMetricHasProblem(long instMetric) {
        String sql = "SELECT id, state, start_datetime, \"end_datetime\", inst_metric,host_id  FROM \"METRIC_STATE\" where  inst_metric =" + instMetric + " and \"end_datetime\" is null ";
        boolean state = false;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return state;
        } else {
            return true;
        }
    }


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
    //TODO: много лишнего
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

    @Transactional
    public GraphPoints getAllValues(int host_id, int metricId) {
        long defTime= 10*1000;//10sek
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where metric = " + metricId + " and host = " + host_id + " order by date_time ";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
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
//        return averaging(jdbcTemplateObject.queryForList(sql));
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

        return new GraphPoints(rowSize, new TreeMap<Long, Object>(map));
    }

    @Transactional
    public GraphPoints getValuesYear(int host_id, int metricId,  Date dateTime) throws ParseException {
        Date nDate = (Date) dateTime.clone();
        nDate.setMonth(dateTime.getMonth() - 6);
        dateTime.setMonth(dateTime.getMonth() + 6);
        String sql = "SELECT value,date_time FROM \"VALUE_METRIC\" where date_time between '" + dateFormat.format(nDate) + "' and '" + dateFormat.format(dateTime) + "' and metric = " + metricId + " and host = " + host_id + " order by date_time ";

//        return averaging(jdbcTemplateObject.queryForList(sql));


        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
        double values = (double) rows.get(0).get("value");
//        Map<Long, Double> map = new HashMap<>();
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

//        for (int i = 1; i < rowSize; i++) {
//            if ((nDate.getYear() == ((java.sql.Timestamp) rows.get(i).get("date_time")).getYear()) & (nDate.getMonth() == ((java.sql.Timestamp) rows.get(i).get("date_time")).getMonth())) {
//                values += (double) rows.get(i).get("value");
//                counter++;
//            } else {
//                map.put((long) nDate.getTime(), values / counter);
//                nDate = ((java.sql.Timestamp) rows.get(i).get("date_time"));
//                nDate.setSeconds(0);
//                nDate.setMinutes(0);
//                nDate.setHours(0);
//                values = (double) rows.get(i).get("value");
//                counter = 1;
//            }
//            map.put((long) nDate.getTime(), values / counter);
//        }

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

//        return averaging(jdbcTemplateObject.queryForList(sql));



        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        int rowSize = rows.size(), counter = 1;
//        double values = (double) rows.get(0).get("value");
//        Map<Long, Double> map = new HashMap<>();
//        nDate = ((java.sql.Timestamp) rows.get(0).get("date_time"));
//        nDate.setSeconds(0);
//        nDate.setMinutes(0);
//        nDate.setHours(0);
////        System.out.println(nDate);
//        for (int i = 1; i < rowSize; i++) {
//            if ((nDate.getDay() == ((java.sql.Timestamp) rows.get(i).get("date_time")).getDay()) & (nDate.getMonth() == ((java.sql.Timestamp) rows.get(i).get("date_time")).getMonth())) {
//                values += (double) rows.get(i).get("value");
//                counter++;
//            } else {
//                map.put((long) nDate.getTime(), values / counter);
//                nDate = ((java.sql.Timestamp) rows.get(i).get("date_time"));
//                nDate.setSeconds(0);
//                nDate.setMinutes(0);
//                nDate.setHours(0);
//                values = (double) rows.get(i).get("value");
//                counter = 1;
//            }
//            map.put((long) nDate.getTime(), values / counter);
//        }

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
            if(row.get("min_value")!=null)
                templateMetric.setMinValue((double) row.get("min_value"));
            if(row.get("max_value")!=null)
                templateMetric.setMaxValue((double) row.get("max_value"));
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
            if(row.get("min_value")!=null)
            templateMetric.setMinValue((double) row.get("min_value"));
            if(row.get("max_value")!=null)
            templateMetric.setMaxValue((double) row.get("max_value"));
            metrics1.add(templateMetric);
        }
        return metrics1;
    }


    @Transactional
    public void updateTemplMetric(int id,String title,String command,double minValue,double maxValue) throws SQLException {
        String sql = "UPDATE \"TEMPLATE_METRICS\" SET min_value='"+minValue+"',max_value='"+maxValue+"',title='"+title+"',query=$q$"+command+"$q$ WHERE id="+id;
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public void addTemplMetric(String title,String command,double minValue,double maxValue) throws SQLException {
        String sql = "INSERT INTO  \"TEMPLATE_METRICS\"( min_value, max_value,title, query) VALUES( '"+minValue+"','"+maxValue+"','"+title+"',$q$"+command+"$q$ )";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public void dellTemplMetric(int id) throws SQLException {
        String sql = "DELETE FROM \"TEMPLATE_METRICS\" where id="+id;
        jdbcTemplateObject.update(sql);
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
    @Transactional
    public double getMinValueTemplateMetric(int id) throws SQLException {
        String sql = "select min_value FROM \"TEMPLATE_METRICS\" where id ="+id;
        return (double)jdbcTemplateObject.queryForMap(sql).get("min_value");
    }
    public double getMaxValueTemplateMetric(int id) throws SQLException {
        String sql = "select max_value FROM \"TEMPLATE_METRICS\" where id ="+id;
        return (double)jdbcTemplateObject.queryForMap(sql).get("max_value");
    }

    @Transactional
    public void updateMinMaxValueTemplateMetric(double min_value,double max_value,int id) throws SQLException {
        String sql = "UPDATE \"TEMPLATE_METRICS\" SET min_value="+min_value+",max_value="+max_value+"WHERE id="+id;
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public double getMinValueInstanceMetric(int id) throws SQLException {
        String sql = "select min_value FROM \"INSTANCE_METRIC\" where id ="+id;
        return (double)jdbcTemplateObject.queryForMap(sql).get("min_value");
    }
    public double getMaxValueInstanceMetric(int id) throws SQLException {
        String sql = "select max_value FROM \"INSTANCE_METRIC\" where id ="+id;
        return (double)jdbcTemplateObject.queryForMap(sql).get("max_value");
    }

    @Transactional
    public void updateMinMaxValueInstanceMetric(double min_value,double max_value,int id) throws SQLException {
        String sql = "UPDATE \"INSTANCE_METRIC\" SET min_value="+min_value+",max_value="+max_value+"WHERE id="+id;
        jdbcTemplateObject.update(sql);
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
    public List<HostRow> getHostRow() throws SQLException {
        List<HostRow> hostrows = new ArrayList<>();
        String sql = "(select count(*) as countServices,host,(select count(*)from \"METRIC_STATE\" where host_id = im.host) as countProblems ,(select count(*)from \"HOST_STATE\" where host = im.host and (\"end_datetime\" is null and \"start_datetime\" is not null)) as status from \"INSTANCE_METRIC\" as im group by host)";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        for (SSHConfiguration host : this.hosts.getAll()) {
            HostRow hostRow = new HostRow();
            hostRow.setId(host.getId());
            hostRow.setHostName(host.getName());
            hostRow.setLocation(host.getLocation());
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
    @Transactional
    public List<HostEditRow> getHostEditRow() throws SQLException {
        List<HostEditRow> hostrows = new ArrayList<>();
        String sql = "(select count(*) as countServices,host,(select count(*)from \"METRIC_STATE\" where host_id = im.host) as countProblems ,(select count(*)from \"HOST_STATE\" where host = im.host and (\"end_datetime\" is null and \"start_datetime\" is not null)) as status from \"INSTANCE_METRIC\" as im group by host)";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        for (SSHConfiguration host : this.hosts.getAll()) {
            HostEditRow hostRow = new HostEditRow();
            hostRow.setId(host.getId());
            hostRow.setName(host.getName());
            hostRow.setHost(host.getHost());
            hostRow.setPort(host.getPort());
            hostRow.setLogin(host.getLogin());
            hostRow.setPassword(host.getPassword());
            hostRow.setLocation(host.getLocation());
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
    public List<MetricRow> getMetricRow(int hostId) throws SQLException {
        List<MetricRow> MetricRows = new ArrayList<>();
        String sql = "select id,title ,(select value from \"VALUE_METRIC\" where metric = im.id ORDER BY id DESC limit 1) as value ,(select date_time from \"VALUE_METRIC\" where metric = im.id ORDER BY id DESC limit 1) as date ,(select count(*)from \"METRIC_STATE\" where inst_metric = im.id ) as countProblems ,(select count(*)from \"METRIC_STATE\" where inst_metric = im.id and (\"end_datetime\" is null and \"start_datetime\" is not null)) as status  from \"INSTANCE_METRIC\" as im where host = " + hostId;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            MetricRow metricrow = new MetricRow();
            metricrow.setId(Integer.parseInt(row.get("id").toString()));
            metricrow.setTitle((row.get("title").toString()));
            metricrow.setErrorsCount(Integer.parseInt(row.get("countProblems").toString()));
            if(row.get("value")!=null)
            metricrow.setLastValue(Double.parseDouble(row.get("value").toString()));
            metricrow.setDate(((java.sql.Timestamp) row.get("date")));
            metricrow.setStatus(row.get("status").toString());
            MetricRows.add(metricrow);
        }
        return MetricRows;
    }
    @Transactional
    public List<MetricRow> getMetricRow(int hostId,int metricId) throws SQLException {
        List<MetricRow> MetricRows = new ArrayList<>();
        String sql = "select id,title ,(select value from \"VALUE_METRIC\" where metric = im.id ORDER BY id DESC limit 1) as value ,(select date_time from \"VALUE_METRIC\" where metric = im.id ORDER BY id DESC limit 1) as date ,(select count(*)from \"METRIC_STATE\" where inst_metric = im.id ) as countProblems ,(select count(*)from \"METRIC_STATE\" where inst_metric = im.id and (\"end_datetime\" is null and \"start_datetime\" is not null)) as status  from \"INSTANCE_METRIC\" as im where id = " + metricId;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            MetricRow metricrow = new MetricRow();
            metricrow.setId(Integer.parseInt(row.get("id").toString()));
            metricrow.setTitle((row.get("title").toString()));
            metricrow.setErrorsCount(Integer.parseInt(row.get("countProblems").toString()));
            metricrow.setLastValue(Double.parseDouble(row.get("value").toString()));
            metricrow.setDate(((java.sql.Timestamp) row.get("date")));
            metricrow.setStatus(row.get("status").toString());
            MetricRows.add(metricrow);
        }
        return MetricRows;
    }
    //Favorites
    @Transactional
    public List<Favorites> getFavoritesRow(String name) throws SQLException {
        List<Favorites> favoriteses = new ArrayList<>();
        String sql = "select *,(select title from \"INSTANCE_METRIC\" where id = f.inst_metric_id) as title  from \"FAVORITES\" as f where f.user_name='"+name+"'";
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
        String sql = "SELECT i.title , a.host_id, a.inst_metric,a.start_datetime,a.end_datetime FROM \"METRIC_STATE\" as a , \"INSTANCE_METRIC\" as i where a.id = " + problemId + " and i.id = a.inst_metric";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            problem.setHostId((int) row.get("host_id"));
            problem.setInstMetricId((int) row.get("inst_metric"));
            problem.setInstMetric((String) row.get("title"));
            problem.setStartDate(((java.sql.Timestamp) rows.get(0).get("start_datetime")));
            problem.setEndDate(((java.sql.Timestamp) rows.get(0).get("end_datetime")));
        }
        return problem;
    }


    @Transactional
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();//
//        String sql = "SELECT u.username , u.password, r.role,r.roleid FROM \"Users\" as u, \"Roles\" as r where u.roleid=r.roleid";
        String sql = "SELECT u.username , u.password, r.role,r.id FROM \"Users\" as u, \"Roles\" as r where u.username=r.username";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        for (Map row : rows) {
            User user = new User();
            user.setUsername((String)row.get("username"));
            user.setPassword((String)row.get("password"));
            user.setRole((String)row.get("role"));
            user.setRoleid((int)row.get("id"));
            usersList.add(user);
        }
        return usersList;
    }
    @Transactional
    public List<String> getRoles() {
        String sql = "SELECT distinct r.role FROM \"Roles\" as r ";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        List<String> stringList = new ArrayList<>();
        for (Map row : rows) {
            stringList.add((String)row.get("role"));
        }

        return stringList;
    }
    @Transactional
    public User getUsers(String userName) {
//        String sql = "SELECT u.username , u.password, r.role,r.roleid FROM \"Users\" as u, \"Roles\" as r where u.roleid=r.roleid";
        String sql = "SELECT u.username , u.password, r.role,r.id FROM \"Users\" as u join \"Roles\" as r on r.username=u.username where u.username = '"+userName+"'";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        User user = new User();
        for (Map row : rows) {
            user.setUsername((String)row.get("username"));
            user.setPassword((String)row.get("password"));
            user.setRole((String)row.get("role"));
            user.setRoleid((int)row.get("id"));
        }

        sql = "SELECT distinct r.role FROM \"Roles\" as r ";
        rows = jdbcTemplateObject.queryForList(sql);
        List<String> stringList = new ArrayList<>();
        for (Map row : rows) {
            stringList.add((String)row.get("role"));
        }
        user.setAllRoles(stringList);

        return user;
    }
    @Transactional
    public void updateUser(int roleid,String username,String password,String role) throws SQLException {
        String sql ="UPDATE \"Roles\"   SET role='"+role+"', username='"+username+"' WHERE username='"+username+"'";
        jdbcTemplateObject.update(sql);

        sql ="UPDATE \"Users\" SET username='"+username+"', password='"+password+"' WHERE username='"+username+"'";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public void addUser(String username,String password,String role) throws SQLException {
        String sql ="INSERT INTO  \"Users\" (username, password, enabled) VALUES ('"+username+"', '"+password+"',true)";
        jdbcTemplateObject.update(sql);

        sql ="INSERT INTO \"Roles\" (role, username)   VALUES ('"+role+"','"+username+"')";
        jdbcTemplateObject.update(sql);
    }

    @Transactional
    public void dellUser(String username) throws SQLException {
        String sql ="DELETE FROM \"Roles\" where username='"+username+"'";
        jdbcTemplateObject.update(sql);

        sql ="DELETE FROM \"Users\" where username='"+username+"'";
        jdbcTemplateObject.update(sql);
    }




    @Transactional
    public long getCountRoles() throws SQLException {
        String sql = "select count(*) from \"Roles\" ";
        return (long)jdbcTemplateObject.queryForMap(sql).get("count");
    }


    @Transactional
    public void setNewUserRole(String username,int roleid) throws SQLException {
        String sql ="UPDATE \"Users\" set  roleid="+roleid+" WHERE username=\'"+username+"\'";
        jdbcTemplateObject.update(sql);
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

    public void updateHost(int id,String host,String login, String password,int port, String name, String location) throws SQLException {
        String sql = "UPDATE sshconfigurationhibernate SET host='"+host+"',login='" +login+
                "',password='"+password+"',port="+port+",name='"+name+"',location='"+location+"' WHERE sshconfigurationhibernate_id="+id;
       jdbcTemplateObject.update(sql);
    }

    @Override
    public List<SSHConfiguration> getHostsByLocation(String location) throws SQLException {
        String sql = "SELECT * FROM \"sshconfigurationhibernate\" WHERE location LIKE\'%"+location+"%\'";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        List<SSHConfiguration> hosts= new ArrayList<>();
        for (Map row : rows) {
            SSHConfiguration host = new SSHConfiguration();
            host.setId((int)row.get("sshconfigurationhibernate_id"));
            host.setPort((int)row.get("port"));
            host.setLogin((String)row.get("login"));
            host.setPassword((String)row.get("password"));
            host.setLocation((String)row.get("location"));
            host.setName((String)row.get("name"));
            host.setHost((String)row.get("host"));
            hosts.add(host);
        }
        return hosts;
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
        String sql = "delete from  \"INSTANCE_METRIC\" where id=" + id + " and host=" + host;
        jdbcTemplateObject.update(sql);
    }

    public int getHostIdByLocation(String location){
        int id;
        return 0;
    }
    //TODO Favorites
    @Transactional
    public void addToFavorites(int host, int metric,String user) throws SQLException {
        String sql = "INSERT INTO \"FAVORITES\"(host_id,inst_metric_id,user_name) VALUES (" + host + "," + metric + ",'" + user + "')";
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
//        String sql = "SELECT count(*)  FROM \"INSTANCE_METRIC\"  where \"end_datetime\" is not null";
//        return Integer.parseInt(jdbcTemplateObject.queryForMap(sql).get("count").toString());
        return hosts.getAll().size();
    }
    @Transactional
    public int metricsProblemCount() throws SQLException {
        String sql = "SELECT count(*)  FROM \"METRIC_STATE\"  where \"end_datetime\" is null";
        return Integer.parseInt(jdbcTemplateObject.queryForMap(sql).get("count").toString());
    }
    @Transactional
    public int metricsSuccesCount() throws SQLException {//TODO стоит делать или нет, хз
        String sql = "SELECT count(*)  FROM \"INSTANCE_METRIC\"";
//        if (hostsSuccesCount() > 0) {
            return Integer.parseInt(jdbcTemplateObject.queryForMap(sql).get("count").toString());
//        }
//        return 0;
    }
}
