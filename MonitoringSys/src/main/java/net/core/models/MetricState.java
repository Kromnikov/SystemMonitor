package net.core.models;

import java.util.Date;

public class MetricState {

    private int id;

    private String state;

    private Date start;

    private Date end;

    private int instMetric;

    private boolean resolved;

    public MetricState() {
    }

    public MetricState(int id, String state, Date start, Date end, int instMetric,boolean resolved) {
        this.id = id;
        this.state = state;
        this.start = start;
        this.end = end;
        this.instMetric = instMetric;
        this.resolved = resolved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getInstMetric() {
        return instMetric;
    }

    public void setInstMetric(int instMetric) {
        this.instMetric = instMetric;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}
