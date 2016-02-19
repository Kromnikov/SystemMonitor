package net.core.models;

import java.util.Date;

public class MetricState {

    private int id;

    private double minValue;

    private double value;

    private double maxValue;

    private Date start;

    private Date end;

    private int instMetric;

    private boolean resolved;

    public MetricState() {
    }

    public MetricState(int id, double minValue, double value, double maxValue, Date start, Date end, int instMetric, boolean resolved) {
        this.id = id;
        this.minValue = minValue;
        this.value = value;
        this.maxValue = maxValue;
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

    public double getValue() {
        return value;
    }

    public void setValue(double state) {
        this.value = state;
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

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
}
