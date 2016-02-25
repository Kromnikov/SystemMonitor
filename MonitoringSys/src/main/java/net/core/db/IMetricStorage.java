package net.core.db;

import net.core.models.*;
import net.core.configurations.SSHConfiguration;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;


public interface IMetricStorage{

    //sql
    //metric-state
    public void setAllowableValueMetric(String endTime, int instMetric);

    public boolean overMaxValue(long instMetric);
    public void setOverMaxValue(String startTime, InstanceMetric instanceMetric,int hostId,double valueMetric);

    public boolean lessMinValue(long instMetric);
    public void setLessMinValue(String startTime, InstanceMetric instanceMetric,int hostId,double valueMetric);

    public boolean correctlyMetric(long instMetric);
    public void setCorrectlyMetric(String endTime, int instMetric);
    public void setIncorrectlyMetric(String startTime, int instMetric);

    public TableModel getMetricTableModel();
    public void setResolvedMetric(int id);
    public long getMetricNotResolvedLength();
    public long getMetricNotResolvedLength(int hostId) throws SQLException;
    public List<MetricState> getMetricProblems(int hostId) throws SQLException, ParseException;
    public List<MetricState> getMetricProblems() throws SQLException, ParseException;

    public int getProblemsLength();

    //host-state
    public boolean availableHost(long hostId);
    public void setNotAvailableHost(String startTime, int host,String hostName);
    public void setAvailableHost(String endTime, int host);
    public TableModel getHostTableModel();
    public List<HostsState> getHostsProblems() throws SQLException, ParseException;
    public void setResolvedHost(int host);
    public long getHostNotResolvedLength();

    public Problem getProblem(int problemId) throws SQLException;

    //values
    public void addValue(int host, int metric, double value,String dateTime) throws SQLException ;
//    public List<Double> getAllValueMetricOnHost(int id) throws SQLException;
    public List<Value> getValues(int host_id,int metricId) throws SQLException ;
    public List<Value> getValuesLastYear(int host_id, int metricId, Date dateTime);
    public List<Value> getValuesLastMonth(int host_id, int metricId, Date dateTime);
    public List<Value> getValuesLastWeek(int host_id, int metricId, Date dateTime);
    public List<Value> getValuesLastDay(int host_id, int metricId, Date dateTime);
    public List<Value> getValuesLastHour(int host_id, int metricId, Date dateTime);
    public List<Value> getValuesLastMinets(int host_id, int metricId, Date dateTime);
    public List<Value> getValuesLastTwentyRec(int host_id, int metricId);
    public Map<Long, Double> getValuesLast(int host_id, int metricId);

//
//    //metrics
    public void addTemplateMetric(String title, String query) throws SQLException;
    public TemplateMetric getTemplateMetric(int id) throws SQLException;
    public TemplateMetric getTemplatMetric(String title) throws SQLException;

    //
    //metrics-host
    public void addInstMetric(InstanceMetric instanceMetric) throws SQLException;
    public void addInstMetric(int host, int metric) throws SQLException ;
    public void addInstMetric(SSHConfiguration host, TemplateMetric templateMetric) throws SQLException ;
    public List<InstanceMetric> getInstMetrics(int hostId) throws SQLException;
    public InstanceMetric getInstMetric(int hostId, String title) throws SQLException;
    public void delInstMetric(int metricId) throws SQLException;

    public long getQuantityOfRow(int id) throws SQLException;
//
//    public ResultSet getAllValueMetricOnHostResult(int id)throws SQLException;
//
    public void delMetricFromHost(int id) throws SQLException;
//
    public Integer getTemplatMetricID(String title) throws  SQLException;
//
    public int getHostIDbyTitle(String title) throws SQLException;
//
    public void addStandartMetrics(int id) throws SQLException;

    List<TemplateMetric> getTemplatMetrics() throws SQLException;


    void delMetricFromHost(int host, int id)throws SQLException;
}
