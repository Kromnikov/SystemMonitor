package net.core.db;

import net.core.configurations.SSHConfiguration;
import net.core.models.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


public interface IMetricStorage {

    //sql
    //metric-state
    public void setAllowableValueMetric(String endTime, int instMetric);

    public boolean overMaxValue(long instMetric);

    public void setOverMaxValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric);

    public boolean lessMinValue(long instMetric);

    public void setLessMinValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric);

    public boolean correctlyMetric(long instMetric);

    public void setCorrectlyMetric(String endTime, int instMetric);

    public void setIncorrectlyMetric(String startTime, int instMetric);

    public TableModel getMetricTableModel();

    public void setResolvedMetric(int id);

    public void setResolvedMetric();

    public long getMetricNotResolvedLength();

    public long getMetricNotResolvedLength(int hostId) throws SQLException;

    public List<MetricState> getMetricProblems(int hostId) throws SQLException, ParseException;

    public List<MetricState> getMetricProblems(int hostId,int instMetricId) throws SQLException, ParseException;

    public List<MetricState> getMetricProblems() throws SQLException, ParseException;

    public int getProblemsLength();

    //host-state
    public boolean availableHost(long hostId);

    public void setNotAvailableHost(String startTime, int host, String hostName);

    public void setAvailableHost(String endTime, int host);

    public TableModel getHostTableModel();

    public List<HostsState> getHostsProblems() throws SQLException, ParseException;

    public void setResolvedHost(int host);

    public void setResolvedHost();

    public long getHostNotResolvedLength();

    public Problem getProblem(int problemId) throws SQLException;

    //values
    public void addValue(int host, int metric, double value, String dateTime) throws SQLException;


    public Date getLastDate(int hostId, int metricId);

    public chartValuesO getAllValues(int host_id, int metricId);

//    public chartValuesO getValuesLastDay(int host_id, int metricId, int zoom, Date dateTime);
//
//    public chartValuesO getValuesDay(int host_id, int metricId, int zoom, Date dateTime);
//
//    public chartValuesO getValuesTheeDays(int host_id, int metricId, int zoom, Date dateTime);
//
//    public chartValuesO getValuesMonth(int host_id, int metricId, int zoom, Date dateTime);
//
//    public chartValuesO getValuesSixMonth(int host_id, int metricId, int zoom, Date dateTime);
//
//
//
//    public chartValues getValuesByZoom(int host_id, int metricId, int zoom);
//
//    public chartValues getValuesByZoom(int host_id, int metricId, int zoom, Date dateTime);
//
//    public chartValues getValuesLastHour(int host_id, int metricId, int zoom, Date dateTime);
//
//    public chartValues getValuesTheeMinutes(int host_id, int metricId, int zoom, Date dateTime);
//
//    public chartValues getValuesOneMinutes(int host_id, int metricId, int zoom, Date dateTime);


    //hostsRows
    public List<HostRow> getHostRow() throws SQLException;

    //metricRows
    public List<metricRow> getMetricRow(int hostId) throws SQLException;

    public chartValuesO getValuesLastDay(int host_id, int metricId, Date dateTime) throws ParseException;

    public chartValuesO getValuesDay(int host_id, int metricId, Date dateTime) throws ParseException;

    public chartValues getValuesTheeDays(int host_id, int metricId, Date dateTime);

    public chartValuesO getValuesMonth(int host_id, int metricId, Date dateTime) throws ParseException;

    public chartValues getValuesSixMonth(int host_id, int metricId, Date dateTime);

    public chartValuesO getValuesYear(int host_id, int metricId, Date dateTime) throws ParseException;


//    public chartValues getValuesByZoom(int host_id, int metricId, int zoom);

//    public chartValues getValuesByZoom(int host_id, int metricId, Date dateTime);

    public chartValuesO getValuesLastHour(int host_id, int metricId, Date dateTime) throws ParseException;

    public chartValuesO getValuesTheeMinutes(int host_id, int metricId, Date dateTime) throws ParseException;

    public chartValuesO getValuesOneMinutes(int host_id, int metricId, Date dateTime) throws ParseException;






    //hostsRows


    //metricRows

    //Favorites
    public List<Favorites> getFavoritesRow() throws SQLException;







    //
//    //metrics
    public void addTemplateMetric(String title, String query) throws SQLException;

    public TemplateMetric getTemplateMetric(int id) throws SQLException;

    public TemplateMetric getTemplatMetric(String title) throws SQLException;

    //
    //metrics-host
    public void addInstMetric(InstanceMetric instanceMetric) throws SQLException;

    public void addInstMetric(int host, int metric) throws SQLException;

    public void addInstMetric(SSHConfiguration host, TemplateMetric templateMetric) throws SQLException;

    public List<InstanceMetric> getInstMetrics(int hostId) throws SQLException;

    public InstanceMetric getInstMetric(int hostId, String title) throws SQLException;

    public void delInstMetric(int metricId) throws SQLException;

    public long getQuantityOfRow(int id) throws SQLException;

    //
//    public ResultSet getAllValueMetricOnHostResult(int id)throws SQLException;
//
    public void delMetricFromHost(int id) throws SQLException;

    //
    public Integer getTemplatMetricID(String title) throws SQLException;

    //
    public int getHostIDbyTitle(String title) throws SQLException;

    //
    public void addStandartMetrics(int id) throws SQLException;

    List<TemplateMetric> getTemplatMetrics() throws SQLException;


    void delMetricFromHost(int host, int id) throws SQLException;

    List<User> getAllUsers();

    double getMinValueTemplateMetric(int id) throws SQLException;

    double getMaxValueTemplateMetric(int id) throws SQLException;

    void updateMinMaxValueTemplateMetric(double min_value, double max_value, int save) throws SQLException;

    double getMinValueInstanceMetric(int id) throws SQLException;

    double getMaxValueInstanceMetric(int id) throws SQLException;

    void updateMinMaxValueInstanceMetric(double min_value, double max_value, int save) throws SQLException;

    long getCountRoles() throws SQLException;

    void setNewUserRole(String username, int roleid) throws SQLException;

    void updateHost(int hostid, String ip, String login, String password, int port, String name, String location) throws SQLException;;

    public List<SSHConfiguration> getHostsByLocation(String location)throws SQLException;

    //TODO Favorites
    public void addToFavorites(int host, int metric) throws SQLException;

    public void dellFromFavorites(int favoritesId) throws SQLException;

    //TODO problems count home page
    public int hostsProblemsCount() throws SQLException;

    public int hostsSuccesCount() throws SQLException;

    public int metricsProblemCount() throws SQLException;

    public int metricsSuccesCount() throws SQLException;
}
