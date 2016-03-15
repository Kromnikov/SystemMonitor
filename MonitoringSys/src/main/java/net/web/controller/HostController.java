package net.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.core.configurations.SSHConfiguration;
import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.*;
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

@Controller
public class HostController {

    @Autowired
    private IMetricStorage metricStorage;
    @Autowired
    private HostService hosts;

    public List<SSHConfiguration> getHosts() {
        return this.hosts.getAll();
    }

    public List<hostRow> getHostRow() throws SQLException {
        return metricStorage.getHostRow();
    }

    public int getAllProblemsCount() throws SQLException {
        return ((int) metricStorage.getMetricNotResolvedLength() + (int) metricStorage.getHostNotResolvedLength());
    }

    public int getProblemsCount(int hostId) throws SQLException {
        return (int) metricStorage.getMetricNotResolvedLength(hostId);
    }

    public List<MetricState> getMetricProblems(int hostId) throws SQLException, ParseException {
        return metricStorage.getMetricProblems(hostId);
    }
    public List<MetricState> getMetricProblems(int hostId,int metricId) throws SQLException, ParseException {
        return metricStorage.getMetricProblems(hostId,metricId);
    }


    public List<MetricState> getMetricProblems() throws SQLException, ParseException {
        return metricStorage.getMetricProblems();
    }

    public int getAllMetricProblemsCount() throws SQLException {
        return (int) metricStorage.getMetricNotResolvedLength();
    }

    public List<InstanceMetric> getMetrics(int hostId) throws SQLException {
        return metricStorage.getInstMetrics(hostId);
    }
    public List<metricRow> getMetricRow(int hostId) throws SQLException {
        return metricStorage.getMetricRow(hostId);
    }

    public int getHostsProblemsCount() throws SQLException {
        return (int) metricStorage.getHostNotResolvedLength();
    }

    public List<HostsState> getHostsProblems() throws SQLException, ParseException {
        return metricStorage.getHostsProblems();
    }








    @RequestMapping(value = "/hosts")
    public ModelAndView hostsPage() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        modelAndView.addObject("hostId", 0);
        modelAndView.addObject("title", "title");
        modelAndView.addObject("getHosts", getHostRow());
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        return modelAndView;
    }
    @RequestMapping(value = "/host")
    public ModelAndView hostsPage(@RequestParam("hostId")  int hostId, @RequestParam(required = false, defaultValue = "hidden") String instMetrics, @RequestParam(required = false, defaultValue = "hidden") String problems, @RequestParam(required = false, defaultValue = "-1") int instMetricId, @RequestParam(required = false, defaultValue = "title") String title) throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        modelAndView.addObject("getHosts", getHostRow());
        modelAndView.addObject("hostId", hostId);
        modelAndView.addObject("instMetrics", instMetrics);
        modelAndView.addObject("problems", problems);
        //metrics
        modelAndView.addObject("title", title);
        modelAndView.addObject("instMetricId", instMetricId);
        modelAndView.addObject("getMetrics", getMetricRow(hostId));

        //problems
        if (instMetricId > 0 & problems.equals("show")) {
            modelAndView.addObject("getMetricProblems", getMetricProblems(hostId,instMetricId));

        } else {
            modelAndView.addObject("getMetricProblems", getMetricProblems(hostId));
        }


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
    public String saveHost(String hostName, String port, String login, String password) throws SQLException {
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
    @RequestMapping(value = "/problems")
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

    @RequestMapping(value = "/problems/resolve/host", method = RequestMethod.GET)
    public String resolveHost(@RequestParam("hostId") int hostId) {
        metricStorage.setResolvedHost(hostId);
        return "redirect:/problems";
    }

    @RequestMapping(value = "/problems/resolve/hosts/all", method = RequestMethod.GET)
    public String resolveHostAll() {
        metricStorage.setResolvedHost();
        return "redirect:/problems";
    }

    @RequestMapping(value = "/problems/resolve/metric", method = RequestMethod.GET)
    public String resolveMetric(@RequestParam("resMetrId") int resMetrId) {
        metricStorage.setResolvedMetric(resMetrId);
        return "redirect:/problems";
    }

    @RequestMapping(value = "/problems/resolve/metric/all", method = RequestMethod.GET)
    public String resolveMetric() {
        metricStorage.setResolvedMetric();
        return "redirect:/problems";
    }

    @RequestMapping(value = "/problem/metric", method = RequestMethod.GET)
    public String redirectToMetric(@RequestParam("problemId") int problemId) throws SQLException {
        Problem problem = metricStorage.getProblem(problemId);
        return "redirect:/intsMetric?hostId=" + problem.getHostId() + "&instMetricId=" + problem.getInstMetricId() + "&title=" + problem.getInstMetric();
    }


//    //TODO:main page host
//    @RequestMapping(value = "/host")
//    @ResponseBody
//    public ModelAndView hostPage(@RequestParam("hostId") int hostId) throws SQLException {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("getProblemsCount", getProblemsCount(hostId));
//        modelAndView.addObject("hostId", hostId);
//        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
//        modelAndView.setViewName("host");
//        return modelAndView;
//    }





    //TODO:page edit inst metrics
    @RequestMapping(value = "/editIntsMetrics")
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






    //TODO ajax charts
//    @RequestMapping(value = "/lastDay", method = RequestMethod.GET)
//         @ResponseBody
//    public chartValues ajaxTest(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam("zoom") int zoom, @RequestParam(required = false, defaultValue = "0") long date) throws JsonProcessingException {
//        chartValues values = null;
//        if (date == 0) {
//            values = metricStorage.getValuesLastDay(hostId, instMetricId, zoom, metricStorage.getLastDate(hostId, instMetricId));
//        } else {
//            values = metricStorage.getValuesLastDay(hostId, instMetricId, zoom, new Date(date));
//        }
//
//
//        return values;
//
//    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    public chartValues getAll(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId) throws JsonProcessingException {
        return metricStorage.getAllValues(hostId,instMetricId);
    }

    @RequestMapping(value = "/getValuesYear", method = RequestMethod.GET)
    @ResponseBody
    public chartValues getValuesYear(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam("zoom") int zoom, @RequestParam(required = false, defaultValue = "0") long date) throws JsonProcessingException {
        chartValues values = null;
        if (date == 0) {
            values = metricStorage.getValuesYear(hostId, instMetricId, zoom, metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesYear(hostId, instMetricId, zoom, new Date(date));
        }


        return values;

    }

    @RequestMapping(value = "/getValuesSixMonth", method = RequestMethod.GET)
    @ResponseBody
    public chartValues getValuesSixMonth(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam("zoom") int zoom, @RequestParam(required = false, defaultValue = "0") long date) throws JsonProcessingException {
        chartValues values = null;
        if (date == 0) {
            values = metricStorage.getValuesSixMonth(hostId, instMetricId, zoom, metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesSixMonth(hostId, instMetricId, zoom, new Date(date));
        }


        return values;

    }

    @RequestMapping(value = "/getValuesMonth", method = RequestMethod.GET)
    @ResponseBody
    public chartValues getValuesMonth(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam("zoom") int zoom, @RequestParam(required = false, defaultValue = "0") long date) throws JsonProcessingException {
        chartValues values = null;
        if (date == 0) {
            values = metricStorage.getValuesMonth(hostId, instMetricId, zoom, metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesMonth(hostId, instMetricId, zoom, new Date(date));
        }


        return values;

    }

    @RequestMapping(value = "/getValuesTheeDays", method = RequestMethod.GET)
    @ResponseBody
    public chartValues getValuesTheeDays(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam("zoom") int zoom, @RequestParam(required = false, defaultValue = "0") long date) throws JsonProcessingException {
        chartValues values = null;
        if (date == 0) {
            values = metricStorage.getValuesTheeDays(hostId, instMetricId, zoom, metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesTheeDays(hostId, instMetricId, zoom, new Date(date));
        }


        return values;

    }

    @RequestMapping(value = "/getValuesDay", method = RequestMethod.GET)
    @ResponseBody
    public chartValues getValuesDay(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam("zoom") int zoom, @RequestParam(required = false, defaultValue = "0") long date) throws JsonProcessingException {
        chartValues values = null;
        if (date == 0) {
            values = metricStorage.getValuesDay(hostId, instMetricId, zoom, metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesDay(hostId, instMetricId, zoom, new Date(date));
        }


        return values;

    }

    @RequestMapping(value = "/lastDay", method = RequestMethod.GET)
    @ResponseBody
    public chartValues lastDay(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam(required = false, defaultValue = "0") long date) throws JsonProcessingException {
        chartValues values = null;
        if (date == 0) {
            values = metricStorage.getValuesLastDay(hostId, instMetricId, metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesLastDay(hostId, instMetricId,  new Date(date));
        }


        return values;

    }

    @RequestMapping(value = "/chartClickHour", method = RequestMethod.GET)
    @ResponseBody
    public chartValues chartClickHour(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam("zoom") int zoom, @RequestParam(required = false, defaultValue = "0") long date) throws JsonProcessingException {
        chartValues values = null;
        if (date == 0) {
            values = metricStorage.getValuesLastHour(hostId, instMetricId, zoom, metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesLastHour(hostId, instMetricId, zoom, new Date(date));
        }

        return values;

    }

    @RequestMapping(value = "/chartClickTheeMinutes", method = RequestMethod.GET)
    @ResponseBody
    public chartValues chartClick(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam("zoom") int zoom, @RequestParam(required = false, defaultValue = "0") long date) throws JsonProcessingException {

        chartValues values = null;
        if (date == 0) {
            values = metricStorage.getValuesTheeMinutes(hostId, instMetricId, zoom, metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesTheeMinutes(hostId, instMetricId, zoom, new Date(date));
        }

        return values;

    }

    @RequestMapping(value = "/chartClickOneMinutes", method = RequestMethod.GET)
    @ResponseBody
    public chartValues chartClickOneMinutes(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam("zoom") int zoom, @RequestParam(required = false, defaultValue = "0") long date) throws JsonProcessingException {

        chartValues values = null;
        if (date == 0) {
            values = metricStorage.getValuesOneMinutes(hostId, instMetricId, zoom, metricStorage.getLastDate(hostId, instMetricId));
        } else {
            values = metricStorage.getValuesOneMinutes(hostId, instMetricId, zoom, new Date(date));
        }

        return values;

    }




//    @RequestMapping(value = "/getValuesByZoom", method = RequestMethod.GET)
//    @ResponseBody
//    public chartValues getValuesByZoom(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam("zoom") int zoom, @RequestParam(required = false, defaultValue = "0") long date) throws JsonProcessingException {
//        chartValues values = null;
//        if (date == 0) {
//            values = metricStorage.getValuesByZoom(hostId, instMetricId, zoom);
//        } else {
//            values = metricStorage.getValuesByZoom(hostId, instMetricId, zoom, new Date(date));
//        }
//
//
//        return values;
//
//    }



    //TODO Favorites

    @RequestMapping(value = "/addToFavorites", method = RequestMethod.GET)
    public String addFavorites(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam("title") String title) throws SQLException {
        metricStorage.addToFavorites(hostId,instMetricId);
        return "redirect:/host?hostId="+hostId+"&instMetrics=show&instMetricId="+instMetricId+"&title="+title;
    }
    @RequestMapping(value = "/dellFromFavorites", method = RequestMethod.GET)
    public String dellFavorites(@RequestParam("favoritesId") int favoritesId) throws SQLException {
        metricStorage.dellFromFavorites(favoritesId);
        return "redirect:/";
    }

}
