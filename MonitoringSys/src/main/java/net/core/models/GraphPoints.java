package net.core.models;

import java.util.Map;


public class GraphPoints {


    private Map<Long, Object> values;

    public GraphPoints() {
    }

    public GraphPoints(Map<Long, Object> values) {
        this.values = values;
    }


    public Map<Long, Object> getValues() {
        return values;
    }

    public void setValues(Map<Long, Object> values) {
        this.values = values;
    }
}
