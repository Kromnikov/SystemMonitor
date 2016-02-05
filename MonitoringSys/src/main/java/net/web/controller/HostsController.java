package net.web.controller;


import net.core.configurations.SSHConfiguration;
import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.InstanceMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    private int hostId = Integer.MIN_VALUE;

    private String hostName;


//    @RequestMapping(value = "/hosts")
//    public ModelAndView hostPage() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("hosts");
//        modelAndView.addObject("getHosts", getHosts());
//        return modelAndView;
//    }

    @RequestMapping(value = "/hosts", method = RequestMethod.GET)
    public String hostPage() {
        return "hosts";
    }

    @ModelAttribute("getHosts")
    public List<SSHConfiguration> getHosts() {
        return this.hosts.getAll();
    }

    @ModelAttribute("getMetrics")
    public List<InstanceMetric> getMetrics() {
        return this.instanceMetric;
    }

    @ModelAttribute("getHostId")
    public int gethostId() {
        return this.hostId;
    }


    @RequestMapping(method = RequestMethod.GET, value = "host={id}")
    public
    @ResponseBody
    ModelAndView setHostId(@PathVariable String id) {
        this.hostId = Integer.parseInt(id);
        ModelAndView modelAndView = new ModelAndView();
        try {
            instanceMetric = metricStorage.getInstMetrics(this.hostId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        modelAndView.setViewName("hosts");
        modelAndView.addObject("getMetrics", this.instanceMetric);
        return modelAndView;
    }

    @RequestMapping(value = "/hosts", params = {"delHost"}, method = RequestMethod.POST)
    public ModelAndView delHost() {
        ModelAndView modelAndView = new ModelAndView();
        if (hostId != Integer.MIN_VALUE) {

            this.instanceMetric = null;

            SSHConfiguration sshConfiguration = getHosts().stream().filter(x -> x.getId() == this.hostId).findFirst().get();
            hosts.remove(sshConfiguration);

            modelAndView.setViewName("hosts");
            modelAndView.addObject("getMetrics", this.instanceMetric);
            modelAndView.addObject("getHosts", getHosts());
        }
        hostId = Integer.MIN_VALUE;
        return modelAndView;
    }





//add
    @RequestMapping(value = "/hosts", params = {"addHost"}, method = RequestMethod.POST)
    public ModelAndView addHost() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addHost");
        return modelAndView;
    }


    @RequestMapping(value = "/hosts", params = {"saveHost"}, method = RequestMethod.POST)
    public ModelAndView saveHost(String hostName,String port,String login,String password) {
        ModelAndView modelAndView = new ModelAndView();
        if (hostName != "" & port != "" & login != "" & password != "") {
            SSHConfiguration sshConfiguration = new SSHConfiguration(hostName, Integer.parseInt(port), login, password);
            hosts.save(sshConfiguration);
            modelAndView.addObject("getHosts", getHosts());
            modelAndView.setViewName("hosts");
        } else {
            modelAndView.setViewName("addHost");
        }
        return modelAndView;
    }





    @RequestMapping(value = "/hosts", params = {"returnHosts"}, method = RequestMethod.POST)
    public ModelAndView returnHosts() {
        ModelAndView modelAndView = new ModelAndView();
        this.instanceMetric = null;
        modelAndView.setViewName("hosts");
        modelAndView.addObject("getHosts", getHosts());
        hostId = Integer.MIN_VALUE;
        return modelAndView;
    }














//    @RequestMapping(value="/hosts", params={"setServices1"},method = RequestMethod.POST)
//    public ModelAndView greeting(@RequestParam(value="id", required=false) String id, Model model) {
//        ModelAndView modelAndView = new ModelAndView();
//        try {
//            instanceMetric = metricStorage.getInstMetrics(Integer.parseInt(id));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        modelAndView.setViewName("hosts");
//        modelAndView.addObject("getMetrics", this.instanceMetric);
//        return modelAndView;
//    }


}
