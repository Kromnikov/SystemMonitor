package net.core.models;

import java.util.Date;

/**
 * Created by Kromnikov on 13.03.2016.
 */
public class metricRow {
    int id;
    String title;
    double lastValue;
    int errorsCount;
    Date date;
    String status;

    public metricRow() {
    }

    public metricRow(int id, String title, double lastValue, int errorsCount, Date date, String status) {
        this.id = id;
        this.title = title;
        this.lastValue = lastValue;
        this.errorsCount = errorsCount;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLastValue() {
        return lastValue;
    }

    public void setLastValue(double lastValue) {
        this.lastValue = lastValue;
    }

    public int getErrorsCount() {
        return errorsCount;
    }

    public void setErrorsCount(int errorsCount) {
        this.errorsCount = errorsCount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
