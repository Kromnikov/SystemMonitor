package net.web.controller;


import net.core.configurations.SSHConfiguration;
import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.InstanceMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

@Controller
public class HostsController {

    @Autowired
    private IMetricStorage metricStorage;
    @Autowired
    private HostService hosts;

    private List<InstanceMetric> instanceMetric;


//    @RequestMapping(value = "/hosts")
//    public ModelAndView hostPage() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("hosts");
//        modelAndView.addObject("LOL", hosts.getAll().toArray());//hosts.getAll().toArray()
//        return modelAndView;
//    }

    @RequestMapping(value = "/hosts", method=RequestMethod.GET)
    public String hostPage() {
        return "hosts";
    }

    @ModelAttribute("getHosts")
    public List<SSHConfiguration> getHosts() {
        return this.hosts.getAll();
    }

//    @ModelAttribute("getMetrics")
//    public List<InstanceMetric> getMetrics() {
//        return this.instanceMetric;
//    }

    @RequestMapping(value="/hosts", params={"setServices1"},method = RequestMethod.POST)
    public String greeting(@RequestParam(value="id", required=true) String id, Model model) {
        return "hosts";
    }

    @RequestMapping(method = RequestMethod.GET, value="host={id}")
    public ModelAndView removeEmp(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            instanceMetric = metricStorage.getInstMetrics(Integer.parseInt(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        modelAndView.setViewName("hosts");
        modelAndView.addObject("getMetrics", this.instanceMetric);
        return modelAndView;
    }



//    @RequestMapping(value="/hosts", params={"setServices"},method = RequestMethod.POST)
//    public ModelAndView setServices(@ModelAttribute("getHosts")List<SSHConfiguration> hosts) {
////        try {
////            instanceMetric = metricStorage.getInstMetrics(host.getId());
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
//
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("hosts");
//        modelAndView.addObject("Values", null);
//        return modelAndView;
//    }



}
