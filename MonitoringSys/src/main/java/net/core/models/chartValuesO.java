package net.core.models;

import java.util.Map;

/**
 * Created by Kromnikov on 20.03.2016.
 */
public class chartValuesO {

    private int count;

    private Map<Long, Object> values;

    public chartValuesO() {
    }

    public chartValuesO(int count, Map<Long, Object> values) {
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
