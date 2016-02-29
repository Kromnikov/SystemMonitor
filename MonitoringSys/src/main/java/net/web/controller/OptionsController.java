package net.web.controller;

import net.core.db.IMetricStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

/**
 * Created by ANTON on 28.02.2016.
 */
@Controller
public class OptionsController {
    @Autowired
    private IMetricStorage metricStorage;
    @RequestMapping(method = RequestMethod.GET, value = "/options")
    public ModelAndView metric() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        int tempid=1;
        modelAndView.addObject("getTemplatMetrics",metricStorage.getTemplatMetrics());
        double min= metricStorage.getMinValueTemplateMetric(1);
        double max= metricStorage.getMaxValueTemplateMetric(1);
        modelAndView.addObject("min",min);
        modelAndView.addObject("max",max);
        modelAndView.addObject("tempid",tempid);
        modelAndView.setViewName("templetMetrics");
        return modelAndView;
    }

    @RequestMapping(params = {"tempid"},method = RequestMethod.GET, value = "/options")
    public ModelAndView metricGet(int tempid) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        double min= metricStorage.getMinValueTemplateMetric(tempid);
        double max= metricStorage.getMaxValueTemplateMetric(tempid);
        modelAndView.addObject("getTemplatMetrics",metricStorage.getTemplatMetrics());
        modelAndView.addObject("min",min);
        modelAndView.addObject("max",max);
        modelAndView.addObject("tempid",tempid);
        modelAndView.setViewName("templetMetrics");
        return modelAndView;
    }
    @RequestMapping(params = {"save"},method = RequestMethod.GET, value = "/options")
    public ModelAndView metricSet(double min_value, double max_value,int save) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        metricStorage.updateMinMaxValueTemplateMetric(min_value,max_value,save);  //через save передаю id хоста, вот такой вот костыль)
        modelAndView.addObject("min",min_value);
        modelAndView.addObject("max",max_value);
        modelAndView.addObject("getTemplatMetrics",metricStorage.getTemplatMetrics());
        modelAndView.setViewName("templetMetrics");
        return modelAndView;
    }
}
