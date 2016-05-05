package net.web.controller;

import net.core.IStorageController;
import net.core.models.TemplateMetric;
import net.core.tools.Authorization;
import net.core.tools.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;


@Controller
public class TemplatesController {

    @Autowired
    private IStorageController metricStorage;
    @Autowired
    private Authorization authentication;

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hotfound");
        return modelAndView;
    }
    //TODO: Контроллер для Templat метрик
    @RequestMapping(value="/templMetrics")
    public ModelAndView templMetrics(@RequestParam(required = false , defaultValue = "-1") int id) throws SQLException {
        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
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
        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        return metricStorage.getTemplateMetric(metricId);
    }

    @RequestMapping(value = "/saveTemplMetric", method = RequestMethod.GET)
    public void saveTemplMetric(@RequestParam("id") int id,@RequestParam("title") String title,@RequestParam("command") String command,@RequestParam("minValue") double minValue,@RequestParam("maxValue") double maxValue) throws SQLException {

        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        metricStorage.updateTemplMetric(id, title, command, minValue, maxValue);
    }

    @RequestMapping(value = "/addTemplMetric", method = RequestMethod.GET)
    public void addTemplMetric(@RequestParam("title") String title,@RequestParam("command") String command,@RequestParam("minValue") double minValue,@RequestParam("maxValue") double maxValue) throws SQLException {

        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        metricStorage.addTemplMetric(title, command, minValue, maxValue);
    }
    @RequestMapping(value = "/dellTemplMetric", method = RequestMethod.GET)
    public void dellTemplMetric(@RequestParam("metricId") int id) throws SQLException {

        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        metricStorage.dellTemplMetric(id);
    }

}
