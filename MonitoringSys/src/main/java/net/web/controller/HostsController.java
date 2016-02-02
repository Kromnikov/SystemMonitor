package net.web.controller;


import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
        modelAndView.addObject("Values", null);//hosts.getAll().toArray()
        return modelAndView;
    }

//    @RequestMapping(value = "/hosts" , method= RequestMethod.POST)
//    public ModelAndView processForm1(@ModelAttribute ArrayList<SSHConfiguration> Values) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("hosts");
//        modelAndView.addObject("Values", Values);
//        return modelAndView;
//    }

    @RequestMapping(value="/hosts", params={"hostAction"},method = RequestMethod.POST)
    public ModelAndView lol() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        modelAndView.addObject("Values", null);
        return modelAndView;
    }
    @RequestMapping(value="/hosts", params={"de"},method = RequestMethod.POST)
    public ModelAndView lol1() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        modelAndView.addObject("Values", null);
        return modelAndView;
    }

}
