package net.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.core.IStorageServices;
import net.core.hibernate.services.HostService;
import net.core.models.GraphPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by ANTON on 27.03.2016.
 */
@Controller
public class ChartController {
    @Autowired
    private IStorageServices metricStorage;
    @Autowired
    private HostService hosts;

    //TODO ajax charts

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    public GraphPoints getAll(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId) throws JsonProcessingException {
        return metricStorage.getAllValues(hostId,instMetricId);
    }

    @RequestMapping(value = "/getValuesYear", method = RequestMethod.GET)
    @ResponseBody
    public GraphPoints getValuesYear(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam(required = false, defaultValue = "0") long date, @RequestParam(required = false, defaultValue = "0") long endDate) throws JsonProcessingException, ParseException {
        GraphPoints values = null;
        if (date == 0) {
            values = metricStorage.getValuesYear(hostId, instMetricId,  metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesYear(hostId, instMetricId,  new Date(date));
        }


        return values;

    }

    @RequestMapping(value = "/getValuesSixMonth", method = RequestMethod.GET)
    @ResponseBody
    public GraphPoints getValuesSixMonth(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam(required = false, defaultValue = "0") long date, @RequestParam(required = false, defaultValue = "0") long endDate) throws JsonProcessingException {
        GraphPoints values = null;
        if (date == 0) {
            values = metricStorage.getValuesSixMonth(hostId, instMetricId,  metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesSixMonth(hostId, instMetricId,  new Date(date));
        }


        return values;

    }

    @RequestMapping(value = "/getValuesMonth", method = RequestMethod.GET)
    @ResponseBody
    public GraphPoints getValuesMonth(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam(required = false, defaultValue = "0") long date, @RequestParam(required = false, defaultValue = "0") long endDate) throws JsonProcessingException, ParseException {
        GraphPoints values = null;
        if (date == 0) {
            values = metricStorage.getValuesMonth(hostId, instMetricId,  metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesMonth(hostId, instMetricId,  new Date(date));
        }


        return values;

    }

    @RequestMapping(value = "/getValuesTheeDays", method = RequestMethod.GET)
    @ResponseBody
    public GraphPoints getValuesTheeDays(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam(required = false, defaultValue = "0") long date, @RequestParam(required = false, defaultValue = "0") long endDate) throws JsonProcessingException {
        GraphPoints values = null;
        if (date == 0) {
            values = metricStorage.getValuesTheeDays(hostId, instMetricId,  metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesTheeDays(hostId, instMetricId,  new Date(date));
        }


        return values;

    }

    @RequestMapping(value = "/getValuesDay", method = RequestMethod.GET)
    @ResponseBody
    public GraphPoints getValuesDay(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam(required = false, defaultValue = "0") long date, @RequestParam(required = false, defaultValue = "0") long endDate) throws JsonProcessingException, ParseException {
        GraphPoints values = null;
        if (date == 0) {
            values = metricStorage.getValuesDay(hostId, instMetricId,  metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesDay(hostId, instMetricId,  new Date(date));
        }


        return values;

    }

    @RequestMapping(value = "/lastDay", method = RequestMethod.GET)
    @ResponseBody
    public GraphPoints lastDay(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam(required = false, defaultValue = "0") long date, @RequestParam(required = false, defaultValue = "0") long endDate) throws JsonProcessingException, ParseException {
        GraphPoints values = null;
        if (date == 0) {
//            Date a=  metricStorage.getLastDate(hostId, instMetricId);
            values = metricStorage.getValuesLastDay(hostId, instMetricId, metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesLastDay(hostId, instMetricId,  new Date(date));
        }


        return values;

    }

    @RequestMapping(value = "/chartClickHour", method = RequestMethod.GET)
    @ResponseBody
    public GraphPoints chartClickHour(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam(required = false, defaultValue = "0") long date, @RequestParam(required = false, defaultValue = "0") long endDate) throws JsonProcessingException, ParseException {
        GraphPoints values = null;
        if (date == 0) {
            values = metricStorage.getValuesLastHour(hostId, instMetricId,  metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesLastHour(hostId, instMetricId,  new Date(date));
        }

        return values;

    }

    @RequestMapping(value = "/chartClickTheeMinutes", method = RequestMethod.GET)
    @ResponseBody
    public GraphPoints chartClick(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam(required = false, defaultValue = "0") long date, @RequestParam(required = false, defaultValue = "0") long endDate) throws JsonProcessingException, ParseException {

        GraphPoints values = null;
        if (date == 0) {
            values = metricStorage.getValuesTheeMinutes(hostId, instMetricId,  metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesTheeMinutes(hostId, instMetricId,  new Date(date));
        }

        return values;

    }

    @RequestMapping(value = "/chartClickOneMinutes", method = RequestMethod.GET)
    @ResponseBody
    public GraphPoints chartClickOneMinutes(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam(required = false, defaultValue = "0") long date, @RequestParam(required = false, defaultValue = "0") long endDate) throws JsonProcessingException, ParseException {

        GraphPoints values = null;
        if (date == 0) {
            values = metricStorage.getValuesOneMinutes(hostId, instMetricId,  metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesOneMinutes(hostId, instMetricId,  new Date(date));
        }

        return values;

    }
}
