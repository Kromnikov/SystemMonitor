package net.web.controller;

import net.core.IStorageController;
import net.core.models.TemplateMetric;
import net.core.tools.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;


@Controller
@RequestMapping(value = "/admin")
public class TemplatesController {

    @Autowired
    private IStorageController metricStorage;
    @Autowired
    private Authorization authentication;

    //TODO: Контроллер для Templat метрик
    @RequestMapping(value="/templMetrics")
    public ModelAndView templMetrics(@RequestParam(required = false , defaultValue = "-1") int id) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getTemplatMetrics",metricStorage.getTemplatMetrics());
        if(id>0)
            modelAndView.addObject("templMetric",metricStorage.getTemplateMetric(id));
        modelAndView.setViewName("tempMetrics");
        modelAndView.addObject("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return modelAndView;
    }

    @RequestMapping(value = "/getTemplMetric", method = RequestMethod.GET)
    @ResponseBody
    public TemplateMetric getTemplMetric(@RequestParam("metricId") int metricId) throws SQLException {
        return metricStorage.getTemplateMetric(metricId);
    }

    @RequestMapping(value = "/saveTemplMetric", method = RequestMethod.GET)
    public void saveTemplMetric(@RequestParam("id") int id,@RequestParam("title") String title,@RequestParam("command") String command,@RequestParam("minValue") double minValue,@RequestParam("maxValue") double maxValue) throws SQLException {
        metricStorage.updateTemplMetric(id, title, command, minValue, maxValue);
    }

    @RequestMapping(value = "/addTemplMetric", method = RequestMethod.GET)
    public void addTemplMetric(@RequestParam("title") String title,@RequestParam("command") String command,@RequestParam("minValue") double minValue,@RequestParam("maxValue") double maxValue) throws SQLException {
        metricStorage.addTemplMetric(title, command, minValue, maxValue);
    }
    @RequestMapping(value = "/dellTemplMetric", method = RequestMethod.GET)
    public void dellTemplMetric(@RequestParam("metricId") int id) throws SQLException {
        metricStorage.dellTemplMetric(id);
    }

}
