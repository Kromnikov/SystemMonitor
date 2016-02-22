package net.core.models;

import java.util.Date;

public class ValueJson {

    private double value;

    private Date dateTime;

    public ValueJson() {
    }

    public ValueJson( double value, Date dateTime) {
        this.value = value;
        this.dateTime = dateTime;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
