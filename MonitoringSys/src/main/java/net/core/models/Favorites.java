package net.core.models;

public class Favorites {
    int id;
    int hostId;
    int metricId;
    String title;

    public Favorites() {
    }

    public Favorites(int id, int hostId, int metricId, String title) {
        this.id = id;
        this.hostId = hostId;
        this.metricId = metricId;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public int getMetricId() {
        return metricId;
    }

    public void setMetricId(int metricId) {
        this.metricId = metricId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
