package net.core.models;

public class Problem {

    private int id;

    private int instMetricId;

    private String instMetric;

    private int hostId;

    private String hostName;

    public Problem() {
    }

    public Problem(int id, int instMetricId, String instMetric, int hostId, String hostName) {
        this.id = id;
        this.instMetricId = instMetricId;
        this.instMetric = instMetric;
        this.hostId = hostId;
        this.hostName = hostName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInstMetricId() {
        return instMetricId;
    }

    public void setInstMetricId(int instMetricId) {
        this.instMetricId = instMetricId;
    }

    public String getInstMetric() {
        return instMetric;
    }

    public void setInstMetric(String instMetric) {
        this.instMetric = instMetric;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
