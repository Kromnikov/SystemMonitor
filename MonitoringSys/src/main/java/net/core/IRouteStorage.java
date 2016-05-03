package net.core;

import net.core.models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


public interface IRouteStorage {

    public void dump() throws IOException;

    //TODO: alarms
    public List<GenericAlarmsRow> getAlarms(String userName);

    public AlarmRow getAlarm(int id) throws SQLException;

    public AlarmRow getNewAlarm() throws SQLException;

    public void updateAlarm(int id, int serviseId, int hostId, String toEmail, String toUser);

    public void addAlarm(int serviseId, int hostId, String toEmail, String toUser, String user);

    public void dellAlarm(int id);


    //sql
    //metric-state
    public boolean isMetricHasProblem(long instMetric);

    public void setAllowableValueMetric(String endTime, int instMetric);

    public boolean overMaxValue(long instMetric);

    public void setOverMaxValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric);

    public boolean lessMinValue(long instMetric);

    public void setLessMinValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric);

    public boolean correctlyMetric(long instMetric);

    public void setCorrectlyMetric(String endTime, int instMetric);

    public void setIncorrectlyMetric(String startTime, int instMetric);


    public void setResolvedMetric(int id);

    public void setResolvedMetric();

    public long getMetricNotResolvedLength();

    public long getMetricNotResolvedLength(int hostId) throws SQLException;

    public List<MetricProblem> getMetricProblems(int hostId) throws SQLException, ParseException;

    public List<MetricProblem> getMetricProblems(int hostId,int instMetricId) throws SQLException, ParseException;

    public List<MetricProblem> getMetricProblems() throws SQLException, ParseException;

    //host-state
    public boolean availableHost(long hostId);

    public void setNotAvailableHost(String startTime, int host, String hostName);

    public void setAvailableHost(String endTime, int host);

    public List<HostsState> getHostsProblems() throws SQLException, ParseException;

    public void setResolvedHost(int host);

    public void setResolvedHost();

    public long getHostNotResolvedLength();

    public ProblemRow getProblem(int problemId) throws SQLException;

    //values
    public void addValue(int host, int metric, double value, String dateTime) throws SQLException;


    public Date getLastDate(int hostId, int metricId);

    public GraphPoints getAllValues(int host_id, int metricId);


    //hostsRows
    public List<HostRow> getHostRow() throws SQLException;
    public List<HostEditRow> getHostEditRow() throws SQLException;

    //metricRows
    public List<MetricRow> getMetricRow(int hostId) throws SQLException;

    public GraphPoints getValuesLastDay(int host_id, int metricId, Date dateTime) throws ParseException;

    public GraphPoints getValuesDay(int host_id, int metricId, Date dateTime) throws ParseException;

    public GraphPoints getValuesTheeDays(int host_id, int metricId, Date dateTime);

    public GraphPoints getValuesMonth(int host_id, int metricId, Date dateTime) throws ParseException;

    public GraphPoints getValuesSixMonth(int host_id, int metricId, Date dateTime);

    public GraphPoints getValuesYear(int host_id, int metricId, Date dateTime) throws ParseException;



    public GraphPoints getValuesLastHour(int host_id, int metricId, Date dateTime) throws ParseException;

    public GraphPoints getValuesTheeMinutes(int host_id, int metricId, Date dateTime) throws ParseException;

    public GraphPoints getValuesOneMinutes(int host_id, int metricId, Date dateTime) throws ParseException;






    //hostsRows


    //metricRows

    //Favorites
    public List<Favorites> getFavoritesRow(String name) throws SQLException;







    //
//    //metrics
    public void addTemplateMetric(String title, String query) throws SQLException;

    public TemplateMetric getTemplateMetric(int id) throws SQLException;

    //
    //metrics-host

    public void addInstMetric(int host, int metric) throws SQLException;

    public void addInstMetric(InstanceMetric instanceMetric) throws SQLException;

    public void editInstMetric(int id, int hostId, int templMetricId, String title, String command, double minValue, double maxValue) throws SQLException;

    public InstanceMetric getInstMetric(int instMetricId) throws SQLException;


    public List<InstanceMetric> getInstMetrics(int hostId) throws SQLException;


    //
    public void addStandartMetrics(int id) throws SQLException;

    List<TemplateMetric> getTemplatMetrics() throws SQLException;

    public void updateTemplMetric(int id, String title, String command, double minValue, double maxValue) throws SQLException;

    public void addTemplMetric(String title, String command, double minValue, double maxValue) throws SQLException;

    public void dellTemplMetric(int id) throws SQLException;

    void delMetricFromHost(int host, int id) throws SQLException;

    //TODO:users

    List<User> getAllUsers();

    public List<String> getRoles();

    public User getUsers(String userName);

    public void updateUser(int roleid, String username, String password, String role) throws SQLException;

    public void addUser(String username, String password, String role) throws SQLException;

    public void dellUser(String username) throws SQLException;

    long getCountRoles() throws SQLException;

    void setNewUserRole(String username, int roleid) throws SQLException;





    //TODO Favorites
    public void addToFavorites(int host, int metric,String user) throws SQLException;

    public void dellFromFavorites(int favoritesId) throws SQLException;

    //TODO problems count home page
    public int hostsProblemsCount() throws SQLException;

    public int hostsSuccesCount() throws SQLException;

    public int metricsProblemCount() throws SQLException;

    public int metricsSuccesCount() throws SQLException;
}
