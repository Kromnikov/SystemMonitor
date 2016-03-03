package net.core.models;

import java.util.Map;

public class chartValues {

    private int count;

    private Map<Long, Double> values;

    public chartValues() {
    }

    public chartValues(int count, Map<Long, Double> values) {
        this.count = count;
        this.values = values;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Map<Long, Double> getValues() {
        return values;
    }

    public void setValues(Map<Long, Double> values) {
        this.values = values;
    }
}
