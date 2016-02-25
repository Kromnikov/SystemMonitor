package net.web.controller;

import net.core.AccauntDb;
import net.core.db.IMetricStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by ANTON on 24.02.2016.
 */

@Controller
public class AccauntEditorController {

    @Autowired
    private IMetricStorage metricStorage;
    @RequestMapping(value = "/accaunts")
    public ModelAndView accauntsView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getAllUsers", metricStorage.getAllUsers());
        modelAndView.setViewName("accaunts");
        return modelAndView;
    }
}