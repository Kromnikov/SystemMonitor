package net.web.controller;


import net.core.configurations.SSHConfiguration;
import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.InstanceMetric;
import net.core.models.TemplateMetric;
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

    List<TemplateMetric> templatMetrics;

    private int hostId = Integer.MIN_VALUE;

    private int instMetricId = Integer.MIN_VALUE;

    private int templMetricId = Integer.MIN_VALUE;

    private String hostName;


    @RequestMapping(value = "/hosts")
    public ModelAndView hostPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        this.instanceMetric = null;
        instMetricId = Integer.MIN_VALUE;
        hostId = Integer.MIN_VALUE;
        modelAndView.addObject("getHosts", getHosts());
        modelAndView.addObject("getMetrics", getMetrics());
        return modelAndView;
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


    @RequestMapping(method = RequestMethod.GET, value = "hosts(id={id})")
    public @ResponseBody ModelAndView setHostId(@PathVariable String id) {
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
//    @RequestMapping(method = RequestMethod.GET, value = "delHost")
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
        modelAndView.setViewName("hosts");
        modelAndView.addObject("getHosts", getHosts());
        return modelAndView;
    }





//add
//    @RequestMapping(value = "/hosts", params = {"addHost"}, method = RequestMethod.POST)
    @RequestMapping(method = RequestMethod.GET, value = "addHostPage")
    public ModelAndView addHostPage() {
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






//host
@RequestMapping(method = RequestMethod.GET, value = "selectedHost")
public @ResponseBody ModelAndView selectedHostPage() {
    ModelAndView modelAndView = new ModelAndView();
    if (this.hostId != Integer.MIN_VALUE) {
        modelAndView.setViewName("hosts/host");
    }
    else{
        return hostPage();
    }
    return modelAndView;
}
    @RequestMapping(method = RequestMethod.GET, value = "problem")
 public @ResponseBody ModelAndView selectedHostProblems() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("problem");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "intsMetrics")
    public @ResponseBody ModelAndView selectedHostIntsMetrics() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getMetrics", this.instanceMetric);
        modelAndView.setViewName("metrics");
        return modelAndView;
    }


    @RequestMapping(method = RequestMethod.GET, value = "editIntsMetrics")
    public @ResponseBody ModelAndView selectedHostEditIntsMetrics() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        templatMetrics= metricStorage.getTemplatMetrics();
        modelAndView.addObject("getTemplatMetrics", this.templatMetrics);
        modelAndView.addObject("getMetrics", this.instanceMetric);
        modelAndView.setViewName("addIntsMetric");
        return modelAndView;
    }









    //Inst metric
    @RequestMapping(method = RequestMethod.GET, value = "instMetric={id}")
    public @ResponseBody ModelAndView setInstMetricId(@PathVariable int id) {
        this.instMetricId = id;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        return modelAndView;
    }

    @RequestMapping(value = "/hosts", params = {"dellInstMetric"}, method = RequestMethod.POST)
    public ModelAndView dellInstMetric() {
        ModelAndView modelAndView = new ModelAndView();
        if(instMetricId!=Integer.MIN_VALUE) {
            try {
                metricStorage.delInstMetric(this.instMetricId);
                instanceMetric = metricStorage.getInstMetrics(this.hostId);
                instMetricId=Integer.MIN_VALUE;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //modelAndView.addObject("getMetrics", this.instanceMetric);
        }
        modelAndView.setViewName("addIntsMetric");
        modelAndView.addObject("getTemplatMetrics", this.templatMetrics);
        modelAndView.addObject("getMetrics", this.instanceMetric);
        //modelAndView.addObject("getHosts", getHosts());
        return modelAndView;
    }

//    @RequestMapping(value = "/hosts", params = {"addEditInstMetric"}, method = RequestMethod.POST)
    @RequestMapping(method = RequestMethod.GET, value = "addEditInstMetric")
    public ModelAndView addEditInstMetric() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        templatMetrics= metricStorage.getTemplatMetrics();
        if(this.hostId!=Integer.MIN_VALUE) {
            modelAndView.setViewName("addIntsMetric");
            modelAndView.addObject("getMetrics", this.instanceMetric);
            modelAndView.addObject("getTemplatMetrics", this.templatMetrics);
        }else {
            return hostPage();
        }
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "selectInstMetric={id}")
    public @ResponseBody ModelAndView selectInstMetricId(@PathVariable int id ) throws SQLException {
        this.instMetricId = id;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addIntsMetric");
        modelAndView.addObject("getMetrics", this.instanceMetric);
        modelAndView.addObject("getTemplatMetrics", this.templatMetrics);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "templatMetric={id}")
    public @ResponseBody ModelAndView selectTemplMetricId(@PathVariable int id ) throws SQLException {
        this.templMetricId = id;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addIntsMetric");
        modelAndView.addObject("getMetrics", this.instanceMetric);
        modelAndView.addObject("getTemplatMetrics", this.templatMetrics);
        return modelAndView;
    }

    @RequestMapping(value = "/hosts", params = {"addInstMetric"}, method = RequestMethod.POST)
    public ModelAndView addInstMetric() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();

        metricStorage.addInstMetric(this.hostId,this.templMetricId);
        instanceMetric = metricStorage.getInstMetrics(this.hostId);


        templatMetrics= metricStorage.getTemplatMetrics();
        modelAndView.setViewName("addIntsMetric");
        modelAndView.addObject("getMetrics", this.instanceMetric);
        modelAndView.addObject("getTemplatMetrics", this.templatMetrics);
        return modelAndView;
    }














}
