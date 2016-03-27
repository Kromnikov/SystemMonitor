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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

@Controller
public class HostController {

    @Autowired
    private IMetricStorage metricStorage;
    @Autowired
    private HostService hosts;

//    private static DateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

    public List<SSHConfiguration> getHosts() {
        return this.hosts.getAll();
    }

    public List<HostRow> getHostRow() throws SQLException {
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

    public List<MetricRow> getMetricRow(int hostId) throws SQLException {
        return metricStorage.getMetricRow(hostId);
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
    @RequestMapping(params={"search","location"},value = "/hosts",method= RequestMethod.GET)
    public ModelAndView hostsPageSearchLocation(String location) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        List<HostRow> hostRows = new ArrayList<HostRow>();
        hostRows= getHostRow();
        int length = hostRows.size();
        int i=0;
        while (i<hostRows.size()) {
            if (hostRows.get(i).getLocation().indexOf(location)==-1) {
                hostRows.remove(i);
                i--;
            }
            i++;
        }
        modelAndView.setViewName("hosts");
        modelAndView.addObject("hostId", 0);
        modelAndView.addObject("title", "title");
        modelAndView.addObject("getHosts", hostRows);
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        return modelAndView;
    }
    @RequestMapping(value = "/host")
    public ModelAndView hostsPage(@RequestParam("hostId")  int hostId, @RequestParam(required = false, defaultValue = "hidden") String instMetrics, @RequestParam(required = false, defaultValue = "hidden") String problems, @RequestParam(required = false, defaultValue = "-1") int instMetricId, @RequestParam(required = false, defaultValue = "title") String title,@RequestParam(required=false,defaultValue = "0") String startDate,@RequestParam(required=false,defaultValue = "0") String endDate) throws SQLException, ParseException {
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
        if (!startDate.equals("0")) {
            modelAndView.addObject("startDate", parserDate(startDate).getTime());
        }
        if (!endDate.equals("0")) {
            modelAndView.addObject("endDate", parserDate(endDate).getTime());
        }


        return modelAndView;
    }

    private Date parserDate(String startDate) throws ParseException {
        String mTimeZone = startDate.substring(20,23);
        String mActualDate = startDate.replace(mTimeZone + " ", "");
        System.out.println(mActualDate);
        String TWITTER = "EEE MMM dd HH:mm:ss yyyy";
        SimpleDateFormat mSf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        mSf.setTimeZone(TimeZone.getDefault());
        Date mNewDate = mSf.parse(mActualDate);
        return mNewDate;
    }

    @RequestMapping(params={"search","hostName"},value = "/hosts",method= RequestMethod.GET)
    public ModelAndView hostsPageSearchHostName(String hostName) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        List<HostRow> hostRows = new ArrayList<HostRow>();
        hostRows= getHostRow();
        int length = hostRows.size();
        int i=0;
        while (i<hostRows.size()) {
            if (hostRows.get(i).getHostName().indexOf(hostName)==-1) {
                hostRows.remove(i);
                i--;
            }
            i++;
        }
        modelAndView.setViewName("hosts");
        modelAndView.addObject("hostId", 0);
        modelAndView.addObject("title", "title");
        modelAndView.addObject("getHosts", hostRows);
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

//    @RequestMapping(value = "/getValuesByZoom", method = RequestMethod.GET)
//    @ResponseBody
//    public ChartValues getValuesByZoom(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam(required = false, defaultValue = "0") long date, @RequestParam(required = false, defaultValue = "0") long endDate) throws JsonProcessingException {
//        ChartValues values = null;
//        if (date == 0) {
//            values = metricStorage.getValuesByZoom(hostId, instMetricId, zoom);
//        } else {
//            values = metricStorage.getValuesByZoom(hostId, instMetricId,  new Date(date));
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
