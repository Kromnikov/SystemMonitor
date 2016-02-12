package net.web.controller;


import net.core.configurations.SSHConfiguration;
import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.InstanceMetric;
import net.core.models.MetricState;
import net.core.models.TemplateMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.text.ParseException;
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

    private List<MetricState> metricStateList;

    private String hostName;

    private int problemsCount = Integer.MIN_VALUE;

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


//    @ModelAttribute("getAllProblemsCount")
//    public int getAllProblemsCount() throws SQLException {
//        this.problemsCount = (int)metricStorage.getMetricNotResolvedLength(this.hostId);
//        return this.problemsCount;
//    }

    @ModelAttribute("getProblemCount")
    public int getProblemsCount() throws SQLException {
        this.problemsCount = (int)metricStorage.getMetricNotResolvedLength(this.hostId);
        return this.problemsCount;
    }
    @ModelAttribute("getMetricProblems")
    public List<MetricState> getMetricProblems() throws SQLException, ParseException {
        this.metricStateList = metricStorage.getMetricProblems(this.hostId);
        return this.metricStateList;
    }



    @RequestMapping(value = "/hosts")
    public ModelAndView hostPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        this.instanceMetric = null;
        instMetricId = Integer.MIN_VALUE;
        hostId = Integer.MIN_VALUE;
        modelAndView.addObject("getHosts", getHosts());
//        modelAndView.addObject("getMetrics", getMetrics());
        return modelAndView;
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
    public ModelAndView delHost() {
        if (hostId != Integer.MIN_VALUE) {
            this.instanceMetric = null;
            SSHConfiguration sshConfiguration = getHosts().stream().filter(x -> x.getId() == this.hostId).findFirst().get();
            hosts.remove(sshConfiguration);
            return hostPage();
        }
        return hostPage();
    }



//add host
    @RequestMapping(method = RequestMethod.GET, value = "addHostPage")
    public ModelAndView addHostPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addHost");
        return modelAndView;
    }

    @RequestMapping(value = "/hosts", params = {"saveHost"}, method = RequestMethod.POST)
    public ModelAndView saveHost(String hostName,String port,String login,String password) {
        hosts.save(new SSHConfiguration(hostName, Integer.parseInt(port), login, password));
        return hostPage();
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
//edit host
    @RequestMapping(method = RequestMethod.GET, value = "editHostPage")
    public ModelAndView editHostPage() {
        ModelAndView modelAndView = new ModelAndView();
        if(this.hostId!=Integer.MIN_VALUE) {
            modelAndView.setViewName("editHost");
            SSHConfiguration sshConfiguration = getHosts().stream().filter(x -> x.getId() == this.hostId).findFirst().get();
            modelAndView.addObject("getHost", sshConfiguration);
        }
        else
            return hostPage();
        return modelAndView;
    }

    @RequestMapping(value = "/hosts", params = {"editHost"}, method = RequestMethod.POST)
    public ModelAndView editHost(int id,String host,String port,String login,String password) {
        SSHConfiguration sshConfiguration = new SSHConfiguration(host, Integer.parseInt(port), login, password);
        sshConfiguration.setId(id);
        hosts.update(sshConfiguration);

        return hostPage();
    }




//host
@RequestMapping(method = RequestMethod.GET, value = "host")
public @ResponseBody ModelAndView selectedHostPage() throws SQLException {
    ModelAndView modelAndView = new ModelAndView();
    if (this.hostId != Integer.MIN_VALUE) {
        modelAndView.addObject("getProblemsCount", getProblemsCount());
        modelAndView.setViewName("hosts/host");
    }
    else{
        return hostPage();
    }
    return modelAndView;
}

    //Problem-host
    @RequestMapping(method = RequestMethod.GET, value = "problem")
         public @ResponseBody ModelAndView selectedHostProblems() throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getProblemsCount", getProblemsCount());
        modelAndView.addObject("getMetricProblems", getMetricProblems());
        modelAndView.setViewName("problem");
        return modelAndView;
    }
    @RequestMapping(method = RequestMethod.GET, value = "setResolvedMetric(id={id})")
    public ModelAndView setResolvedMetric(@PathVariable String id) throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
//        metricStorage.setResolvedMetric(Integer.parseInt(id));
        modelAndView.addObject("getProblemsCount", getProblemsCount());
        modelAndView.addObject("getMetricProblems", getMetricProblems());
        modelAndView.setViewName("problem");
        return modelAndView;
    }





    @RequestMapping(method = RequestMethod.GET, value = "intsMetrics")
    public @ResponseBody ModelAndView selectedHostIntsMetrics() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getProblemsCount", getProblemsCount());
        modelAndView.addObject("getMetrics", this.instanceMetric);
        modelAndView.setViewName("metrics");
        return modelAndView;
    }


    @RequestMapping(method = RequestMethod.GET, value = "editIntsMetrics")
    public @ResponseBody ModelAndView selectedHostEditIntsMetrics() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        templatMetrics= metricStorage.getTemplatMetrics();
        modelAndView.addObject("getTemplatMetrics", this.templatMetrics);
        modelAndView.addObject("getProblemsCount", getProblemsCount());
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
