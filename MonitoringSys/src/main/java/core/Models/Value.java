package core.models;

import java.time.LocalDateTime;

public class Value {

    private int id;

    private int host_id;

    private int metric_id;

    private double value;

    private LocalDateTime dateTime;

    public Value() {
    }

    public Value(int host_id, int metric_id, double value, LocalDateTime dateTime) {
        this.host_id = host_id;
        this.metric_id = metric_id;
        this.value = value;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHost_id() {
        return host_id;
    }

    public void setHost_id(int host_id) {
        this.host_id = host_id;
    }

    public int getMetric_id() {
        return metric_id;
    }

    public void setMetric_id(int metric_id) {
        this.metric_id = metric_id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
