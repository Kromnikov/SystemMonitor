package net.core.db.interfaces;


import net.core.models.InstanceMetric;

import java.util.List;

public interface IInstanceStorage {

    public void addInstMetric(int host, int metricId);

    public List<InstanceMetric> getInstMetrics(int hostId);

    public InstanceMetric getInstMetric(int instMetricId);

    public void delMetricFromHost(int host, int id);

    public void addInstMetric(InstanceMetric instanceMetric);

    public void editInstMetric(int id, int hostId, int templMetricId, String title, String command, double minValue, double maxValue);
}
