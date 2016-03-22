package net.web.controller;

import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.Favorites;
import net.core.models.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class IndexController {

    @Autowired
    private IMetricStorage metricStorage;
    @Autowired
    private HostService hosts;

    public List<Favorites> getFavoritesRow() throws SQLException {
        return metricStorage.getFavoritesRow();
    }

    public int hostsProblemsCount() throws SQLException, ParseException {
        return metricStorage.hostsProblemsCount();
    }

    public int hostsSuccesCount() throws SQLException, ParseException {
        return metricStorage.hostsSuccesCount();
    }


    public int metricsProblemCount() throws SQLException, ParseException {
        return metricStorage.metricsProblemCount();
    }


    public int metricsSuccesCount() throws SQLException, ParseException {
        return metricStorage.metricsSuccesCount();
    }




    @RequestMapping(value = "/")
    public ModelAndView index(@ModelAttribute("Values") ArrayList<Value> values) throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("favoritesList", getFavoritesRow());
        modelAndView.addObject("hostsProblemsCount", hostsProblemsCount());
        modelAndView.addObject("hostsSuccesCount", hostsSuccesCount());
        modelAndView.addObject("metricsProblemCount", metricsProblemCount());
        modelAndView.addObject("metricsSuccesCount", metricsSuccesCount());
        modelAndView.setViewName("index");
        return modelAndView;
    }
    @RequestMapping(value = "/test")
    public ModelAndView test(@ModelAttribute("Values") ArrayList<Value> values) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("TEST");
        return modelAndView;
    }

}
