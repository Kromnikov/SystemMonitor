package net.core.models;

import java.util.List;

public class MetricsRow {
    int hostId;
    List<InstanceMetric> instanceMetrics;
    List<TemplateMetric> templateMetrics;

    public MetricsRow() {
    }

    public MetricsRow(List<InstanceMetric> instanceMetrics, List<TemplateMetric> templateMetrics) {
        this.instanceMetrics = instanceMetrics;
        this.templateMetrics = templateMetrics;
    }

    public List<InstanceMetric> getInstanceMetrics() {
        return instanceMetrics;
    }

    public void setInstanceMetrics(List<InstanceMetric> instanceMetrics) {
        this.instanceMetrics = instanceMetrics;
    }

    public List<TemplateMetric> getTemplateMetrics() {
        return templateMetrics;
    }

    public void setTemplateMetrics(List<TemplateMetric> templateMetrics) {
        this.templateMetrics = templateMetrics;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }
}
