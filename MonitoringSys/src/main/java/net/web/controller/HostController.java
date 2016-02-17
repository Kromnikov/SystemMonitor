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
import java.text.ParseException;
import java.util.List;

@Controller
public class HostController {

    @Autowired
    private IMetricStorage metricStorage;
    @Autowired
    private HostService hosts;

    public List<SSHConfiguration> getHosts() {
        return this.hosts.getAll();
    }

    public int getAllProblemsCount() throws SQLException {
        return ((int)metricStorage.getMetricNotResolvedLength()+(int)metricStorage.getHostNotResolvedLength());
    }

        public int getProblemsCount(int hostId) throws SQLException {
        return (int)metricStorage.getMetricNotResolvedLength(hostId);
    }

    @RequestMapping(value = "/hosts")
    public ModelAndView hostPage() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        modelAndView.addObject("getHosts", getHosts());
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
//        modelAndView.addObject("getMetrics", getMetrics());
        return modelAndView;
    }



    //addHost
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
        return hostPage();
    }
    //TODO:editHost
//    @RequestMapping(method = RequestMethod.GET, value = "editHostPage")
//    public ModelAndView editHostPage(@PathVariable int hostId) throws SQLException {
//        ModelAndView modelAndView = new ModelAndView();
//            modelAndView.setViewName("editHost");
//            SSHConfiguration sshConfiguration = getHosts().stream().filter(x -> x.getId() == hostId).findFirst().get();
//            modelAndView.addObject("getHost", sshConfiguration);
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/hosts", params = {"editHost"}, method = RequestMethod.POST)
//    public ModelAndView editHost(int id,String host,String port,String login,String password) throws SQLException {
//        SSHConfiguration sshConfiguration = new SSHConfiguration(host, Integer.parseInt(port), login, password);
//        sshConfiguration.setId(id);
//        hosts.update(sshConfiguration);
//
//        return hostPage();
//    }




    @RequestMapping(method = RequestMethod.GET, value = "hosts(hostId={hostId})")
    public @ResponseBody  ModelAndView setHostId(@PathVariable int hostId) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("getProblemsCount", getProblemsCount(hostId));
            modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
            modelAndView.setViewName("hosts/host");
        return modelAndView;
    }











    //    //Problem-host
//    @RequestMapping(method = RequestMethod.GET, value = "problem")
//         public @ResponseBody ModelAndView selectedHostProblems(@PathVariable int hostId) throws SQLException, ParseException {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("getProblemsCount", getProblemsCount());
//        modelAndView.addObject("getMetricProblems", getMetricProblems(this.hostId));
//        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
//        modelAndView.setViewName("problem");
//        return modelAndView;
//    }
//    @RequestMapping(method = RequestMethod.GET, value = "setResolvedMetric(id={id})")
//    public ModelAndView setResolvedMetric(@PathVariable String id) throws SQLException, ParseException {
//        ModelAndView modelAndView = new ModelAndView();
//        metricStorage.setResolvedMetric(Integer.parseInt(id));
//        modelAndView.addObject("getProblemsCount", getProblemsCount());
//        modelAndView.addObject("getMetricProblems", getMetricProblems(this.hostId));
//        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
//        modelAndView.setViewName("problem");
//        return modelAndView;
//    }
}
