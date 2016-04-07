package net.core.models;

import net.core.configurations.SSHConfiguration;

import java.util.List;

public class HostsTemplMetricsRow {
    List<SSHConfiguration> hosts;
    List<TemplateMetric> templateMetrics;

    public HostsTemplMetricsRow() {
    }

    public List<SSHConfiguration> getHosts() {
        return hosts;
    }

    public void setHosts(List<SSHConfiguration> hosts) {
        this.hosts = hosts;
    }

    public List<TemplateMetric> getTemplateMetrics() {
        return templateMetrics;
    }

    public void setTemplateMetrics(List<TemplateMetric> templateMetrics) {
        this.templateMetrics = templateMetrics;
    }
}
