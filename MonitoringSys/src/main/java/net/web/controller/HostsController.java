package net.web.controller;


import net.core.configurations.SSHConfiguration;
import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.InstanceMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value="hosts/{id}", method = RequestMethod.GET)
    public String deleteUser1 (@PathVariable String id) {
        System.out.println(id);
        return "hosts";
    }

//    @RequestMapping(value="/hosts", params={"setServices1"},method = RequestMethod.POST)
//    public String greeting(@RequestParam(value="id", required=false, defaultValue="World") String name, Model model) {
//        return "hosts";
//    }
//
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

//    @ModelAttribute("getHosts")
//    public List<SSHConfiguration> getHosts() {
//        ArrayList<SSHConfiguration> values;
//        values = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            values.add(i, new SSHConfiguration());
//        }
//        return values;
//    }

//    @RequestMapping(value = "/hosts")
//    public ModelAndView checkUser(@ModelAttribute("Values") ArrayList<Value> values) {
//        values = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            values.add(i,new Value());
//            values.get(i).setValue(i);
//        }
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("hosts");
//        modelAndView.addObject("Values", values);
//        return modelAndView;
//    }

//    @RequestMapping(value = "/hosts" , method= RequestMethod.POST)
//    public ModelAndView processForm1(@ModelAttribute ArrayList<SSHConfiguration> Values) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("hosts");
//        modelAndView.addObject("Values", Values);
//        return modelAndView;
//    }

//    @RequestMapping(value="/hosts", params={"hostAction"},method = RequestMethod.POST)
//    public ModelAndView lol() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("hosts");
//        modelAndView.addObject("Values", null);
//        return modelAndView;
//    }

}
