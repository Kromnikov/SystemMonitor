package net.core.models;

import java.util.Map;


public class GraphPoints {

    private int count;

    private Map<Long, Object> values;

    public GraphPoints() {
    }

    public GraphPoints(int count, Map<Long, Object> values) {
        this.count = count;
        this.values = values;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Map<Long, Object> getValues() {
        return values;
    }

    public void setValues(Map<Long, Object> values) {
        this.values = values;
    }
}
