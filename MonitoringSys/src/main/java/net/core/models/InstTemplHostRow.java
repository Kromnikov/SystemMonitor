package net.core.models;

import net.core.configurations.SSHConfiguration;

import java.util.List;

public class InstTemplHostRow {
    SSHConfiguration host;
    InstanceMetric instanceMetrics;
    TemplateMetric templateMetrics;

    public InstTemplHostRow() {
    }

    public InstanceMetric getInstanceMetrics() {
        return instanceMetrics;
    }

    public void setInstanceMetrics(InstanceMetric instanceMetrics) {
        this.instanceMetrics = instanceMetrics;
    }

    public TemplateMetric getTemplateMetrics() {
        return templateMetrics;
    }

    public void setTemplateMetrics(TemplateMetric templateMetrics) {
        this.templateMetrics = templateMetrics;
    }

    public SSHConfiguration getHost() {
        return host;
    }

    public void setHost(SSHConfiguration host) {
        this.host = host;
    }
}
