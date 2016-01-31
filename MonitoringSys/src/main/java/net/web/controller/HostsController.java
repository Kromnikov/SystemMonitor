package net.web.controller;


import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HostsController {

    @Autowired
    private IMetricStorage metricStorage;
    @Autowired
    private HostService hosts;


    @RequestMapping(value = "/hosts")
    public ModelAndView getHosts() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        modelAndView.addObject("Values", hosts.getAll().toArray());
        return modelAndView;
    }

    @RequestMapping(value = "/processForm" , method= RequestMethod.POST)
    public ModelAndView processForm1(@ModelAttribute(value="foo") Value value) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        modelAndView.addObject("Values", hosts.getAll().toArray());
        return modelAndView;
    }

//    @RequestMapping(value = "/processForm", method= RequestMethod.POST)
//    public String processForm(@ModelAttribute(value="foo") Value value) {
//
//        return "hosts";
//    }
}
