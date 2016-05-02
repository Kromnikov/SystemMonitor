package net.core.db.interfaces;


import net.core.models.InstanceMetric;

public interface IMetricStateStorage {

    public boolean isMetricHasProblem(long instMetric);

    public void setAllowableValueMetric(String endTime, int instMetric);

    public boolean overMaxValue(long instMetric);

    public void setOverMaxValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric);

    public boolean lessMinValue(long instMetric);

    public void setLessMinValue(String startTime, InstanceMetric instanceMetric, int hostId, double valueMetric);

    public boolean correctlyMetric(long instMetric);

    public void setCorrectlyMetric(String endTime, int instMetric);

    public void setIncorrectlyMetric(String startTime, int instMetric);
}
