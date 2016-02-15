package net.web.controller;


import net.core.configurations.SSHConfiguration;
import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.HostsState;
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


    @ModelAttribute("getAllProblemsCount")
    public int getAllProblemsCount() throws SQLException {
        return ((int)metricStorage.getMetricNotResolvedLength()+(int)metricStorage.getHostNotResolvedLength());
    }

    @ModelAttribute("getProblemCount")
    public int getProblemsCount() throws SQLException {
        this.problemsCount = (int)metricStorage.getMetricNotResolvedLength(this.hostId);
        return this.problemsCount;
    }
    @ModelAttribute("getProblemCount")
    public int getAllMetricProblemsCount() throws SQLException {
        this.problemsCount = (int)metricStorage.getMetricNotResolvedLength();
        return this.problemsCount;
    }
//    @ModelAttribute("getMetricProblems")
    public List<MetricState> getMetricProblems(int hostId) throws SQLException, ParseException {
        this.metricStateList = metricStorage.getMetricProblems(hostId);
        return this.metricStateList;
    }
//    @ModelAttribute("getMetricProblems")
    public List<MetricState> getMetricProblems() throws SQLException, ParseException {
        this.metricStateList = metricStorage.getMetricProblems();
        return this.metricStateList;
    }

    @ModelAttribute("getHostsProblemsCount")
    public int getHostsProblemsCount() throws SQLException {
//        this.problemsCount = (int)metricStorage.getHostNotResolvedLength();
        return (int)metricStorage.getHostNotResolvedLength();
    }

    @ModelAttribute("getHostsProblems")
    public List<HostsState> getHostsProblems() throws SQLException, ParseException {
        return metricStorage.getHostsProblems();
    }


    @RequestMapping(method = RequestMethod.GET, value = "/viewCourse/id={id}")
    public @ResponseBody ModelAndView setHostId1(@PathVariable String id) throws SQLException {
        this.hostId = Integer.parseInt(id);
        ModelAndView modelAndView = new ModelAndView();
        try {
            instanceMetric = metricStorage.getInstMetrics(this.hostId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        modelAndView.setViewName("hosts");
        modelAndView.addObject("getMetrics", this.instanceMetric);
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
//        return hostPage();
        return modelAndView;
    }

    //allProblems
    @RequestMapping(method = RequestMethod.GET, value = "problems")
    public @ResponseBody ModelAndView problems() throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        modelAndView.addObject("getMetricProblems", getMetricProblems());
        modelAndView.addObject("getProblemsCount", getAllMetricProblemsCount());
        modelAndView.addObject("getHostsProblemsCount", getHostsProblemsCount());
        modelAndView.addObject("getHostsProblems", getHostsProblems());
        modelAndView.setViewName("problems");
        return modelAndView;
    }
    @RequestMapping(method = RequestMethod.GET, value = "setResolvedMetric1(id={id})")
    public ModelAndView setResolvedMetric1(@PathVariable String id) throws SQLException, ParseException {
        metricStorage.setResolvedMetric(Integer.parseInt(id));
        return problems();
    }
    @RequestMapping(method = RequestMethod.GET, value = "setResolvedHost(id={id})")
    public ModelAndView setResolvedHost(@PathVariable String id) throws SQLException, ParseException {
        metricStorage.setResolvedHost(Integer.parseInt(id));
        return problems();
    }




    @RequestMapping(value = "/hosts")
    public ModelAndView hostPage() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        this.instanceMetric = null;
        instMetricId = Integer.MIN_VALUE;
        hostId = Integer.MIN_VALUE;
        modelAndView.addObject("getHosts", getHosts());
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
//        modelAndView.addObject("getMetrics", getMetrics());
        return modelAndView;
    }



    @RequestMapping(method = RequestMethod.GET, value = "hosts(id={id})")
    public @ResponseBody ModelAndView setHostId(@PathVariable String id) throws SQLException {
        this.hostId = Integer.parseInt(id);
        ModelAndView modelAndView = new ModelAndView();
        try {
            instanceMetric = metricStorage.getInstMetrics(this.hostId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        modelAndView.setViewName("hosts");
        modelAndView.addObject("getMetrics", this.instanceMetric);
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
//        return hostPage();
        return modelAndView;
    }

    @RequestMapping(value = "/hosts", params = {"delHost"}, method = RequestMethod.POST)
    public ModelAndView delHost() throws SQLException {
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
    public ModelAndView saveHost(String hostName,String port,String login,String password) throws SQLException {
        hosts.save(new SSHConfiguration(hostName, Integer.parseInt(port), login, password));
        return hostPage();
    }

    @RequestMapping(value = "/hosts", params = {"returnHosts"}, method = RequestMethod.POST)
    public ModelAndView returnHosts() throws SQLException {
//        ModelAndView modelAndView = new ModelAndView();
        this.instanceMetric = null;
//        modelAndView.setViewName("hosts");
//        modelAndView.addObject("getHosts", getHosts());
        hostId = Integer.MIN_VALUE;
        return hostPage();
    }


//edit host
    @RequestMapping(method = RequestMethod.GET, value = "editHostPage")
    public ModelAndView editHostPage() throws SQLException {
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
    public ModelAndView editHost(int id,String host,String port,String login,String password) throws SQLException {
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
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
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
        modelAndView.addObject("getMetricProblems", getMetricProblems(this.hostId));
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        modelAndView.setViewName("problem");
        return modelAndView;
    }
    @RequestMapping(method = RequestMethod.GET, value = "setResolvedMetric(id={id})")
    public ModelAndView setResolvedMetric(@PathVariable String id) throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        metricStorage.setResolvedMetric(Integer.parseInt(id));
        modelAndView.addObject("getProblemsCount", getProblemsCount());
        modelAndView.addObject("getMetricProblems", getMetricProblems(this.hostId));
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        modelAndView.setViewName("problem");
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
    public ModelAndView dellInstMetric() throws SQLException {
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
        modelAndView.setViewName("intsMetric");
        modelAndView.addObject("getTemplatMetrics", this.templatMetrics);
        modelAndView.addObject("getMetrics", this.instanceMetric);
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        //modelAndView.addObject("getHosts", getHosts());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "selectInstMetric={id}")
    public @ResponseBody ModelAndView selectInstMetricId(@PathVariable int id ) throws SQLException {
        this.instMetricId = id;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("intsMetric");
        modelAndView.addObject("getMetrics", this.instanceMetric);
        modelAndView.addObject("getTemplatMetrics", this.templatMetrics);
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "templatMetric={id}")
    public @ResponseBody ModelAndView selectTemplMetricId(@PathVariable int id ) throws SQLException {
        this.templMetricId = id;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("intsMetric");
        modelAndView.addObject("getMetrics", this.instanceMetric);
        modelAndView.addObject("getTemplatMetrics", this.templatMetrics);
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        return modelAndView;
    }

    @RequestMapping(value = "/hosts", params = {"addInstMetric"}, method = RequestMethod.POST)
    public ModelAndView addInstMetric() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();

        metricStorage.addInstMetric(this.hostId,this.templMetricId);
        instanceMetric = metricStorage.getInstMetrics(this.hostId);


        templatMetrics= metricStorage.getTemplatMetrics();
        modelAndView.setViewName("intsMetric");
        modelAndView.addObject("getMetrics", this.instanceMetric);
        modelAndView.addObject("getTemplatMetrics", this.templatMetrics);
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "intsMetrics")
    public @ResponseBody ModelAndView selectedHostIntsMetrics() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getProblemsCount", getProblemsCount());
        modelAndView.addObject("getMetrics", this.instanceMetric);
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
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
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        modelAndView.setViewName("intsMetric");
        return modelAndView;
    }















}
