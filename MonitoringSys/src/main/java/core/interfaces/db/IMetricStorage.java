package core.interfaces.db;

import core.configurations.SSHConfiguration;
import core.models.InstanceMetric;
import core.models.TemplateMetric;
import core.models.Value;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Алексей on 25.11.2015.
 */
public interface IMetricStorage {

    //sql
    //host
    public boolean getState(long hostId);
    public void setFalseStateHost(String startTime,int host);
    public void setTrueStateHost(String endTime,int host);


    //values
    public void addValue(int host, int metric, double value,String dateTime) throws SQLException ;
//    public List<Double> getAllValueMetricOnHost(int id) throws SQLException;
    public List<Value> getValues(int host_id,int metricId) throws SQLException ;
//
//    //metrics
    public void addTemplateMetric(String title, String query) throws SQLException;
    public TemplateMetric getTemplateMetric(int id) throws SQLException;
    public TemplateMetric getTemplatMetric(String title) throws SQLException;

    //
    //metrics-host
    public void addMetricToHost(InstanceMetric instanceMetric) throws SQLException;
    public void addMetricToHost(int host,int metric) throws SQLException ;
    public void addMetricToHost(SSHConfiguration host,TemplateMetric templateMetric) throws SQLException ;
//    public List<Integer> getMetricIdByHostId(int hostId) throws SQLException ;
    public List<InstanceMetric> getMetricsByHostId(int hostId) throws SQLException;
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

    List<TemplateMetric> geAllTemplatMetrics() throws SQLException;


}
