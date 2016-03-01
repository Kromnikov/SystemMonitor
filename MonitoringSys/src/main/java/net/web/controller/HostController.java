package net.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.core.configurations.SSHConfiguration;
import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.HostsState;
import net.core.models.InstanceMetric;
import net.core.models.MetricState;
import net.core.models.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public List<MetricState> getMetricProblems(int hostId) throws SQLException, ParseException {
        return metricStorage.getMetricProblems(hostId);
    }

    public List<MetricState> getMetricProblems() throws SQLException, ParseException {
        return  metricStorage.getMetricProblems();
    }

    public int getAllMetricProblemsCount() throws SQLException {
        return (int)metricStorage.getMetricNotResolvedLength();
    }

    public List<InstanceMetric> getMetrics(int hostId) throws SQLException {
        return metricStorage.getInstMetrics(hostId);
    }

    public int getHostsProblemsCount() throws SQLException {
        return (int)metricStorage.getHostNotResolvedLength();
    }

    public List<HostsState> getHostsProblems() throws SQLException, ParseException {
        return metricStorage.getHostsProblems();
    }





    //TODO:page hosts
    @RequestMapping(value = "/hosts")
    public ModelAndView hostsPage() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        modelAndView.addObject("getHosts", getHosts());
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        return modelAndView;
    }




    //TODO:page addHost
    @RequestMapping(method = RequestMethod.GET, value = "addHostPage")
    public ModelAndView addHostPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addHost");
        return modelAndView;
    }
    @RequestMapping(value = "/hosts", params = {"saveHost"}, method = RequestMethod.POST)
    public String saveHost(String hostName,String port,String login,String password) throws SQLException {
        hosts.save(new SSHConfiguration(hostName, Integer.parseInt(port), login, password));
        return "redirect:/hosts";
    }
    @RequestMapping(value = "/hosts", params = {"returnHosts"}, method = RequestMethod.POST)
    public String returnHosts() throws SQLException {
        return "redirect:/hosts";
    }
    //TODO:page editHost
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

    //TODO:page delHost
//    @RequestMapping(value = "/hosts", params = {"delHost"}, method = RequestMethod.POST)
//    public ModelAndView delHost() throws SQLException {
//        if (hostId != Integer.MIN_VALUE) {
//            this.instanceMetric = null;
//            SSHConfiguration sshConfiguration = getHosts().stream().filter(x -> x.getId() == this.hostId).findFirst().get();
//            hosts.remove(sshConfiguration);
//            return hostPage();
//        }
//        return hostPage();
//    }











    //TODO:page problems
    @RequestMapping(value="/problems")
    @ResponseBody
    public ModelAndView problemsPage() throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        modelAndView.addObject("getMetricProblems", getMetricProblems());
        modelAndView.addObject("getProblemsCount", getAllMetricProblemsCount());
        modelAndView.addObject("getHostsProblemsCount", getHostsProblemsCount());
        modelAndView.addObject("getHostsProblems", getHostsProblems());
        modelAndView.setViewName("problems");
        return modelAndView;
    }
    @RequestMapping(value="/problems/resolve/host" , method = RequestMethod.GET)
    public String resolveHost(@RequestParam("hostId") int hostId){
        metricStorage.setResolvedHost(hostId);
        return "redirect:/problems";
    }
    @RequestMapping(value="/problems/resolve/hosts/all" , method = RequestMethod.GET)
    public String resolveHostAll(){
        metricStorage.setResolvedHost();
        return "redirect:/problems";
    }
    @RequestMapping(value="/problems/resolve/metric" , method = RequestMethod.GET)
    public String resolveMetric(@RequestParam("resMetrId") int resMetrId){
        metricStorage.setResolvedMetric(resMetrId);
        return "redirect:/problems";
    }
    @RequestMapping(value="/problems/resolve/metric/all" , method = RequestMethod.GET)
     public String resolveMetric(){
        metricStorage.setResolvedMetric();
        return "redirect:/problems";
    }
    @RequestMapping(value="/problem/metric" , method = RequestMethod.GET)
    public String redirectToMetric(@RequestParam("problemId") int problemId) throws SQLException {
        Problem problem = metricStorage.getProblem(problemId);
        return "redirect:/intsMetric?hostId="+problem.getHostId()+"&instMetricId="+problem.getInstMetricId()+"&title="+problem.getInstMetric();
    }















    //TODO:main page host
    @RequestMapping(value="/host")
    @ResponseBody
    public ModelAndView hostPage(@RequestParam("hostId") int hostId) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("getProblemsCount", getProblemsCount(hostId));
            modelAndView.addObject("hostId", hostId);
            modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
            modelAndView.setViewName("host");
        return modelAndView;
    }
















    //TODO:page problem
    @RequestMapping(value="/problem")
    @ResponseBody
    public ModelAndView problemsHostPage(@RequestParam("hostId") int hostId) throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getProblemsCount", getProblemsCount(hostId));
        modelAndView.addObject("getMetricProblems", getMetricProblems(hostId));
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        modelAndView.addObject("hostId", hostId);
        modelAndView.setViewName("problem");
        return modelAndView;
    }
    @RequestMapping(value="/problem/resolve" , method = RequestMethod.GET)
    public String resolveInstMetric(@RequestParam("hostId") int hostId,@RequestParam("resMetrId") int resMetrId){
        metricStorage.setResolvedMetric(resMetrId);
        return "redirect:/problem?hostId="+hostId;
    }



























    //TODO:page edit inst metrics
    @RequestMapping(value="/editIntsMetrics")
    @ResponseBody
    public ModelAndView editInstMetricPage(@RequestParam("hostId") int hostId) throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getTemplatMetrics", metricStorage.getTemplatMetrics());
        modelAndView.addObject("getProblemsCount", getProblemsCount(hostId));
        modelAndView.addObject("getMetrics", metricStorage.getInstMetrics(hostId));
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        modelAndView.addObject("hostId", hostId);
        modelAndView.setViewName("addIntsMetric");
        return modelAndView;
    }







    //TODO:page inst metrics
    @RequestMapping(value="/intsMetrics")
    @ResponseBody
    public ModelAndView instMetricPage(@RequestParam("hostId") int hostId) throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getProblemsCount", getProblemsCount(hostId));
        modelAndView.addObject("getMetrics", getMetrics(hostId));
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        modelAndView.addObject("hostId", hostId);
        modelAndView.addObject("title", "title");
        modelAndView.setViewName("metrics");
        return modelAndView;
    }
    @RequestMapping(value="/intsMetric")
    @ResponseBody
    public ModelAndView instMetricSelect(@RequestParam("hostId") int hostId,@RequestParam("instMetricId") int instMetricId,@RequestParam(required=false, defaultValue = "title") String title) throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getProblemsCount", getProblemsCount(hostId));
        modelAndView.addObject("getMetrics", getMetrics(hostId));
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        modelAndView.addObject("hostId", hostId);
        modelAndView.addObject("instMetricId", instMetricId);
        if(!title.equals("title")) {
            modelAndView.addObject("title", title);
        }
        modelAndView.setViewName("metrics");
        return modelAndView;
    }










    @RequestMapping(value = "/ajaxtest", method = RequestMethod.GET)
    @ResponseBody
    public  Map<Long, Double> ajaxTest(@RequestParam("hostId") int hostId,@RequestParam("instMetricId") int instMetricId,@RequestParam("zoom") int zoom,@RequestParam(required=false, defaultValue = "0") long date) throws JsonProcessingException {
        Map<Long, Double> values = null;
        if(date==0) {
            values = metricStorage.getValuesLast(hostId, instMetricId, zoom, metricStorage.getLastDate(hostId, instMetricId));
        }
        else
        {
            values = metricStorage.getValuesLast(hostId, instMetricId, zoom, new Date(date));
        }


        return values;

    }
    @RequestMapping(value = "/chartClickHour", method = RequestMethod.GET)
    @ResponseBody
    public  Map<Long, Double> chartClickHour(@RequestParam("hostId") int hostId,@RequestParam("instMetricId") int instMetricId,@RequestParam("zoom") int zoom,@RequestParam(required=false, defaultValue = "0") long date) throws JsonProcessingException {
        Map<Long, Double> values = null;
        if(date==0) {
            values = metricStorage.getValuesLastHour(hostId, instMetricId, zoom, metricStorage.getLastDate(hostId, instMetricId));
        }
        else
        {
            values = metricStorage.getValuesLastHour(hostId, instMetricId, zoom, new Date(date));
        }

        return values;

    }

    @RequestMapping(value = "/chartClickMinutes", method = RequestMethod.GET)
       @ResponseBody
       public  Map<Long, Double> chartClick(@RequestParam("hostId") int hostId,@RequestParam("instMetricId") int instMetricId,@RequestParam("zoom") int zoom,@RequestParam(required=false, defaultValue = "0") long date) throws JsonProcessingException {

        Map<Long, Double> values = null;
        if(date==0) {
            values = metricStorage.getValuesMinutes(hostId, instMetricId, zoom, metricStorage.getLastDate(hostId, instMetricId));
        }
        else
        {
            values = metricStorage.getValuesMinutes(hostId, instMetricId, zoom, new Date(date));
        }

        return values;

    }
    @RequestMapping(value = "/chartClickSec", method = RequestMethod.GET)
    @ResponseBody
    public  Map<Long, Double> chartClickSec(@RequestParam("hostId") int hostId,@RequestParam("instMetricId") int instMetricId,@RequestParam("zoom") int zoom,@RequestParam(required=false, defaultValue = "0") long date) throws JsonProcessingException {

        Map<Long, Double> values = null;
        if(date==0) {
            values = metricStorage.getValuesSec(hostId, instMetricId, zoom, metricStorage.getLastDate(hostId, instMetricId));
        }
        else
        {
            values = metricStorage.getValuesSec(hostId, instMetricId, zoom, new Date(date));
        }

        return values;
    }

}
