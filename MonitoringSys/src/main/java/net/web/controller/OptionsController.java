package net.web.controller;

import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
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
    @Autowired
    private HostService hosts;;
    //Контроллер для Templat метрик
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
    //Контроллер для Instance метрик
    @RequestMapping(method = RequestMethod.GET, value = "/optionsInstance")
    public ModelAndView standartView() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        int instid=1;
        modelAndView.addObject("getInstanceMetrics",metricStorage.getInstMetrics(1));
        modelAndView.addObject("getHosts",hosts.getAll());
        modelAndView.addObject("hostid",1);
        double min= metricStorage.getMinValueInstanceMetric(1);
        double max= metricStorage.getMaxValueInstanceMetric(1);
        modelAndView.addObject("min",min);
        modelAndView.addObject("max",max);
        modelAndView.addObject("instid",instid);
        modelAndView.setViewName("instanceMetricsOption");
        return modelAndView;
    }
    @RequestMapping(params = {"hostid"},method = RequestMethod.GET, value = "/optionsInstance")
    public ModelAndView selectHostView(int hostid) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getInstanceMetrics",metricStorage.getInstMetrics(hostid));
        modelAndView.addObject("getHosts",hosts.getAll());
        modelAndView.addObject("hostid",hostid);
        double min= metricStorage.getMinValueInstanceMetric(1);
        double max= metricStorage.getMaxValueInstanceMetric(1);
        modelAndView.addObject("min",min);
        modelAndView.addObject("max",max);
        modelAndView.addObject("instid",1);
        modelAndView.setViewName("instanceMetricsOption");
        return modelAndView;
    }
    @RequestMapping(params = {"hostid","instid"},method = RequestMethod.GET, value = "/optionsInstance")
    public ModelAndView selectMetricView(int hostid,int instid) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getInstanceMetrics",metricStorage.getInstMetrics(hostid));
        modelAndView.addObject("getHosts",hosts.getAll());
        modelAndView.addObject("hostid",hostid);
        double min= metricStorage.getMinValueInstanceMetric(instid);
        double max= metricStorage.getMaxValueInstanceMetric(instid);
        modelAndView.addObject("min",min);
        modelAndView.addObject("max",max);
        modelAndView.addObject("instid",instid);
        modelAndView.setViewName("instanceMetricsOption");
        return modelAndView;
    }
    @RequestMapping(params = {"save"},method = RequestMethod.GET, value = "/optionsInstance")
    public ModelAndView saveInstMetic(double min_value, double max_value,int save) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        metricStorage.updateMinMaxValueInstanceMetric(min_value,max_value,save);  //через save передаю id хоста, вот такой вот костыль)
        modelAndView.addObject("min",min_value);
        modelAndView.addObject("max",max_value);
        modelAndView.addObject("getInstanceMetrics",metricStorage.getInstMetrics(1));
        modelAndView.addObject("getHosts",hosts.getAll());
        modelAndView.addObject("getTemplatMetrics",metricStorage.getTemplatMetrics());
        modelAndView.setViewName("instanceMetricsOption");
        return modelAndView;
    }

}
