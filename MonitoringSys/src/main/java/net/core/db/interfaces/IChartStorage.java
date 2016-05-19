package net.core.db.interfaces;

import net.core.models.GraphPoints;

import java.text.ParseException;
import java.util.Date;

public interface IChartStorage {

    public void addValue(int host, int metric, double value, String dateTime);

    public Date getLastDate(int hostId, int metricId);


    public GraphPoints getValuesLastDay(int host_id, int metricId, Date dateTime) throws ParseException;

    public GraphPoints getValuesSixMonth(int host_id, int metricId, Date dateTime) throws ParseException;

    public GraphPoints getValuesYear(int host_id, int metricId, Date dateTime) throws ParseException;

    public GraphPoints getValuesMonth(int host_id, int metricId, Date dateTime) throws ParseException;

    public GraphPoints getValuesTheeDays(int host_id, int metricId, Date dateTime) throws ParseException;

    public GraphPoints getValuesDay(int host_id, int metricId, Date dateTime) throws ParseException;

    public GraphPoints getValuesLastHour(int host_id, int metricId, Date dateTime) throws ParseException;

    public GraphPoints getValuesTheeMinutes(int host_id, int metricId, Date dateTime) throws ParseException;

    public GraphPoints getValuesOneMinutes(int host_id, int metricId, Date dateTime) throws ParseException;

}
