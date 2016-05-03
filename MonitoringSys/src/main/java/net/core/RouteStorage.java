package net.core;


import net.core.alarms.dao.AlarmsLogDao;
import net.core.alarms.dao.GenericAlarmDao;
import net.core.configurations.SSHConfiguration;
import net.core.db.interfaces.*;
import net.core.hibernate.services.HostService;
import net.core.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
@Service("MetricStorage")
public class RouteStorage implements IRouteStorage {


    @Autowired
    private IAlarmsStorage alarmsStorage;
    @Autowired
    private IMetricProblemStorage metricProblemStorage;
    @Autowired
    private ITemplateStorage templateStorage;
    @Autowired
    private IInstanceStorage instanceStorage;
    @Autowired
    private IHostsStateStorage hostsStateStorage;
    @Autowired
    private IMetricStateStorage metricStateStorage;
    @Autowired
    private IChartStorage chartStorage;
    @Autowired
    private IUsersStorage usersStorage;
    @Autowired
    private IHomePageStorage homePageStorage;

    private JdbcTemplate jdbcTemplateObject;
    @Autowired
    private AlarmsLogDao alarmsLogDao;
    @Autowired
    private HostService hosts;
    @Autowired
    private GenericAlarmDao genericAlarm;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public RouteStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }




    @Override
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
    @Transactional
    public void addStandartMetrics(int id) throws SQLException {
        String sql = "INSERT INTO \"INSTANCE_METRIC\" (TEMPL_METRIC,HOST) VALUES (?,?);";
        jdbcTemplateObject.update(sql,1,id);
        String sql1 = "INSERT INTO \"INSTANCE_METRIC\" (TEMPL_METRIC,HOST) VALUES (?,?);";
        jdbcTemplateObject.update(sql1,2,id);
        String sql2 = "INSERT INTO \"INSTANCE_METRIC\" (TEMPL_METRIC,HOST) VALUES (?,?);";
        jdbcTemplateObject.update(sql2,5,id);
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
    public List<metricRow> getMetricRow(int hostId) throws SQLException {
        List<metricRow> MetricRows = new ArrayList<>();
        String sql = "select id,title ,(select value from \"VALUE_METRIC\" where metric = im.id ORDER BY id DESC limit 1) as value ,(select date_time from \"VALUE_METRIC\" where metric = im.id ORDER BY id DESC limit 1) as date ,(select count(*)from \"METRIC_STATE\" where inst_metric = im.id ) as countProblems ,(select count(*)from \"METRIC_STATE\" where inst_metric = im.id and (\"end_datetime\" is null and \"start_datetime\" is not null)) as status  from \"INSTANCE_METRIC\" as im where host = " + hostId;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        for (Map row : rows) {
            metricRow metricrow = new metricRow();
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

//TODO: Ready//TODO: Ready//TODO: Ready//TODO: Ready//TODO: Ready//TODO: Ready//TODO: Ready//TODO: Ready//TODO: Ready//TODO: Ready//TODO: Ready//TODO: Ready//TODO: Ready//TODO: Ready//TODO: Ready//TODO: Ready







    //TODO: alarms
    @Transactional
    public List<GenericAlarmsRow> getAlarms(String userName) {
        return alarmsStorage.getAlarms(userName);
    }
    @Transactional
    public AlarmRow getAlarm(int id) throws SQLException {
        return alarmsStorage.getAlarm(id);
    }
    @Transactional
    public AlarmRow getNewAlarm() throws SQLException {
        return alarmsStorage.getNewAlarm();
    }

    @Transactional
    public void updateAlarm(int id, int serviseId, int hostId, String toEmail, String toUser) {
        alarmsStorage.updateAlarm(id, serviseId, hostId, toEmail, toUser);
    }
    @Transactional
    public void addAlarm(int serviseId, int hostId, String toEmail, String toUser,String user) {
        alarmsStorage.addAlarm(serviseId, hostId, toEmail, toUser, user);
    }
    @Transactional
    public void dellAlarm(int id) {
        alarmsStorage.dellAlarm(id);
    }










    //TODO: metric problem storage
    @Transactional
    public List<MetricProblem> getMetricProblems(int hostId) throws SQLException, ParseException {
        return metricProblemStorage.getMetricProblems(hostId);
    }
    @Transactional
    public List<MetricProblem> getMetricProblems(int hostId,int metricId) throws SQLException, ParseException {
        return metricProblemStorage.getMetricProblems(hostId, metricId);
    }
    @Transactional
    public List<MetricProblem> getMetricProblems() throws SQLException, ParseException {
        return metricProblemStorage.getMetricProblems();
    }
    @Transactional
    public void setResolvedMetric(int id) {
        metricProblemStorage.setResolvedMetric(id);
    }
    @Transactional
    public void setResolvedMetric() {
        metricProblemStorage.setResolvedMetric();
    }
    @Transactional
    public long getMetricNotResolvedLength() {
        return metricProblemStorage.getMetricNotResolvedLength();
    }
    @Transactional
    public long getMetricNotResolvedLength(int hostId) throws SQLException {
        return metricProblemStorage.getMetricNotResolvedLength(hostId);
    }










    //TODO: instMetric
    @Transactional
    public void addInstMetric(int host, int metricId) throws SQLException {
        instanceStorage.addInstMetric(host, metricId);
    }
    @Transactional
    public List<InstanceMetric> getInstMetrics(int hostId) throws SQLException {
        return instanceStorage.getInstMetrics(hostId);
    }
    @Transactional
    public InstanceMetric getInstMetric(int instMetricId) throws SQLException {
        return instanceStorage.getInstMetric(instMetricId);
    }
    @Transactional
    public void delMetricFromHost(int host, int id) throws SQLException {
        instanceStorage.delMetricFromHost(host, id);
    }

    @Transactional
    public void addInstMetric(InstanceMetric instanceMetric) throws SQLException {
        instanceStorage.addInstMetric(instanceMetric);
    }
    @Transactional
    public void editInstMetric(int id,int hostId,int templMetricId,String title,String command,double minValue,double maxValue) throws SQLException {
        instanceStorage.editInstMetric(id, hostId, templMetricId, title, command, minValue, maxValue);
    }







    //TODO: template
    @Transactional
    public void addTemplateMetric(String title, String query) throws SQLException {
        templateStorage.addTemplateMetric(title,query);
    }
    @Transactional
    public TemplateMetric getTemplateMetric(int id) throws SQLException {
        return templateStorage.getTemplateMetric(id);
    }
    @Transactional
    public List<TemplateMetric> getTemplatMetrics() throws SQLException {
        return templateStorage.getTemplatMetrics();
    }
    @Transactional
    public void updateTemplMetric(int id,String title,String command,double minValue,double maxValue) throws SQLException {
        templateStorage.updateTemplMetric(id, title, command, minValue, maxValue);
    }

    @Transactional
    public void addTemplMetric(String title, String command, double minValue, double maxValue) throws SQLException {

        templateStorage.addTemplMetric(title, command, minValue, maxValue);
    }
    @Transactional
    public void dellTemplMetric(int id) throws SQLException {
        templateStorage.dellTemplMetric(id);
    }









    //TODO: host-state
    @Transactional
    public boolean availableHost(long hostId) {//Нужен запрос на вывод состояния хоста
        return hostsStateStorage.availableHost(hostId);
    }
    @Transactional
    public void setNotAvailableHost(String startTime, int host, String hostName) {
        hostsStateStorage.setNotAvailableHost(startTime,host,hostName);
    }
    @Transactional
    public void setAvailableHost(String endTime, int host) {
        hostsStateStorage.setAvailableHost(endTime, host);
    }
    @Transactional
    public List<HostsState> getHostsProblems() throws SQLException, ParseException {
        return hostsStateStorage.getHostsProblems();
    }
    @Transactional
    public void setResolvedHost(int id) {
        hostsStateStorage.setResolvedHost(id);
    }
    @Transactional
    public void setResolvedHost() {
        hostsStateStorage.setResolvedHost();
    }
    @Transactional
    public long getHostNotResolvedLength() {
        return hostsStateStorage.getHostNotResolvedLength();
    }










    //TODO: metric-state
    @Transactional //MAX
    public boolean isMetricHasProblem(long instMetricId) {
        return metricStateStorage.isMetricHasProblem(instMetricId);
    }
    @Transactional
    public void setAllowableValueMetric(String endTime, int instMetricId) {
        metricStateStorage.setAllowableValueMetric(endTime, instMetricId);
    }
    @Transactional //MAX
    public boolean overMaxValue(long instMetricId) {
        return metricStateStorage.overMaxValue(instMetricId);
    }
    @Transactional
    public void setOverMaxValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric) {
        metricStateStorage.setOverMaxValue(startTime, instanceMetric, hostId, valueMetric);
    }
    @Transactional //MIN
    public boolean lessMinValue(long instMetricId) {
        return metricStateStorage.lessMinValue(instMetricId);
    }
    @Transactional  //MIN
    public void setLessMinValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric) {
        metricStateStorage.setLessMinValue(startTime, instanceMetric, hostId, valueMetric);
    }
    @Transactional
    public boolean correctlyMetric(long instMetricId) {
        return metricStateStorage.correctlyMetric(instMetricId);
    }
    @Transactional
    public void setCorrectlyMetric(String endTime, int instMetricId) {
        metricStateStorage.setCorrectlyMetric(endTime,instMetricId);
    }
    @Transactional
    public void setIncorrectlyMetric(String startTime, int instMetricId) {
        metricStateStorage.setIncorrectlyMetric(startTime, instMetricId);
    }




    //TODO:Charts values
    @Transactional
    public void addValue(int host, int metric, double value, String dateTime) throws SQLException {
        chartStorage.addValue(host,metric,value,dateTime);
    }

    @Transactional
    public Date getLastDate(int hostId, int metricId) {
        return chartStorage.getLastDate(hostId, metricId);
    }

    @Transactional
    public GraphPoints getAllValues(int host_id, int metricId) {
        return chartStorage.getAllValues(host_id, metricId);
    }

    @Transactional
    public GraphPoints getValuesLastDay(int host_id, int metricId, Date dateTime) throws ParseException {
        return chartStorage.getValuesLastDay(host_id, metricId, dateTime);
    }

    @Transactional
    public GraphPoints getValuesSixMonth(int host_id, int metricId,  Date dateTime) {
        return chartStorage.getValuesSixMonth(host_id, metricId, dateTime);
    }

    @Transactional
    public GraphPoints getValuesYear(int host_id, int metricId,  Date dateTime) throws ParseException {
        return chartStorage.getValuesYear(host_id, metricId, dateTime);
    }

    @Transactional
    public GraphPoints getValuesMonth(int host_id, int metricId,  Date dateTime) throws ParseException {
        return chartStorage.getValuesMonth(host_id, metricId, dateTime);
    }

    @Transactional
    public GraphPoints getValuesTheeDays(int host_id, int metricId,  Date dateTime) {
        return chartStorage.getValuesTheeDays(host_id, metricId, dateTime);
    }

    @Transactional
    public GraphPoints getValuesDay(int host_id, int metricId,  Date dateTime) throws ParseException {
        return chartStorage.getValuesDay(host_id, metricId, dateTime);
    }

    @Transactional
    public GraphPoints getValuesLastHour(int host_id, int metricId,  Date dateTime) throws ParseException {
        return chartStorage.getValuesLastHour(host_id, metricId, dateTime);
    }

    @Transactional
    public GraphPoints getValuesTheeMinutes(int host_id, int metricId,  Date dateTime) throws ParseException {
        return chartStorage.getValuesTheeMinutes(host_id, metricId, dateTime);
    }

    @Transactional
    public GraphPoints getValuesOneMinutes(int host_id, int metricId,  Date dateTime) throws ParseException {
        return chartStorage.getValuesOneMinutes(host_id, metricId, dateTime);
    }






//TODO: users
    @Transactional
    public List<User> getAllUsers() {
        return usersStorage.getAllUsers();
    }
    @Transactional
    public List<String> getRoles() {
        return usersStorage.getRoles();
    }
    @Transactional
    public User getUsers(String userName) {
        return usersStorage.getUsers(userName);
    }
    @Transactional
    public void updateUser(int roleid,String username,String password,String role) throws SQLException {
        usersStorage.updateUser(username,password,role);
    }
    @Transactional
    public void addUser(String username,String password,String role) throws SQLException {
        usersStorage.addUser(username,password,role);
    }
    @Transactional
    public void dellUser(String username) throws SQLException {
        usersStorage.dellUser(username);
    }
    @Transactional
    public long getCountRoles() throws SQLException {
        return usersStorage.getCountRoles();
    }
    @Transactional
    public void setNewUserRole(String username,int roleid) throws SQLException {
        usersStorage.setNewUserRole(username,roleid);
    }





//TODO:HomePage
    //TODO Favorites

    //Favorites
    @Transactional
    public List<Favorites> getFavoritesRow(String name) throws SQLException {
        return homePageStorage.getFavoritesRow(name);
    }
    @Transactional
    public void addToFavorites(int host, int metric,String user) throws SQLException {
        homePageStorage.addToFavorites(host,metric,user);
    }
    @Transactional
    public void dellFromFavorites(int favoritesId) throws SQLException {
        homePageStorage.dellFromFavorites(favoritesId);
    }

    //TODO problems count home page
    @Transactional
    public int hostsProblemsCount() throws SQLException {
        return homePageStorage.hostsProblemsCount();
    }
    @Transactional
    public int hostsSuccesCount() throws SQLException {
        return homePageStorage.hostsSuccesCount();
    }
    @Transactional
    public int metricsProblemCount() throws SQLException {
        return homePageStorage.metricsProblemCount();
    }
    @Transactional
    public int metricsSuccesCount() throws SQLException {//TODO стоит делать или нет, хз
        return homePageStorage.metricsSuccesCount();
    }
}
