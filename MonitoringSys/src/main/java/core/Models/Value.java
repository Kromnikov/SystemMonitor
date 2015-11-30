package core.models;

import java.time.LocalDateTime;
import java.util.Date;

public class Value {

    private int id;

    private double value;

    private Date dateTime;

    public Value() {
    }

    public Value( double value, Date dateTime) {
        this.value = value;
        this.dateTime = dateTime;
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
