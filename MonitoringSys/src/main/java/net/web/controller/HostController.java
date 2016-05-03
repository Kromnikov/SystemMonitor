package net.web.controller;

import net.core.alarms.AlarmsLog;
import net.core.alarms.dao.AlarmsLogDao;
import net.core.configurations.SSHConfiguration;
import net.core.IRouteStorage;
import net.core.hibernate.services.HostService;
import net.core.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HostController {

    @Autowired
    private IRouteStorage metricStorage;
    @Autowired
    private HostService hosts;
    @Autowired
    private AlarmsLogDao alarmsLogDao;

//    private static DateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

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

    public List<MetricProblem> getMetricProblems(int hostId) throws SQLException, ParseException {
        return metricStorage.getMetricProblems(hostId);
    }


    public List<MetricProblem> getMetricProblems(int hostId, int metricId) throws SQLException, ParseException {
        return metricStorage.getMetricProblems(hostId, metricId);
    }

    public List<MetricProblem> getMetricProblems() throws SQLException, ParseException {
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


    @RequestMapping(value = "/getHost", method = RequestMethod.GET)
    @ResponseBody
    public SSHConfiguration getHost(@RequestParam("id") int id) {
        return hosts.get(id);
    }

    @RequestMapping(value = "/dellHost", method = RequestMethod.GET)
    public String dellHost(@RequestParam("id") int id) {
        hosts.remove(hosts.get(id));
        return "redirect:/hostedit";
    }

    //TODO: alarms
    @RequestMapping(value = "/getAlarms", method = RequestMethod.GET)
    @ResponseBody
    public List<AlarmsModel> getAlarms(@RequestParam("userName") String userName) {
        List<AlarmsModel> alarmsModels = new ArrayList<>();
        for (AlarmsLog g : alarmsLogDao.getByUser(userName)) {
            alarmsModels.add(new AlarmsModel(g.getType(), g.getMessage(), g.getId()));
        }
        return alarmsModels;
    }

    @RequestMapping(value = "/dellAlarm", method = RequestMethod.GET)
    @ResponseBody
    public List<AlarmsModel> dellAlarm(@RequestParam("id") int id, @RequestParam("userName") String userName) {
        AlarmsLog alarmsLog = alarmsLogDao.get(id);
        alarmsLogDao.remove(alarmsLog);
        List<AlarmsModel> alarmsModels = new ArrayList<>();
        for (AlarmsLog g : alarmsLogDao.getByUser(userName)) {
            alarmsModels.add(new AlarmsModel(g.getType(), g.getMessage(), g.getId()));
        }
        return alarmsModels;
    }


    @RequestMapping(value = "/hosts")
    public ModelAndView hostsPage() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        modelAndView.addObject("hostId", 0);
        modelAndView.addObject("title", "title");
        modelAndView.addObject("getHosts", getHostRow());
        modelAndView.addObject("getAllProblemsCount", getAllProblemsCount());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        modelAndView.addObject("username", name);
        return modelAndView;
    }

    @RequestMapping(params = {"search", "location"}, value = "/hosts", method = RequestMethod.GET)
    public ModelAndView hostsPageSearchLocation(String location) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        List<hostRow> hostRows = new ArrayList<hostRow>();
        hostRows = getHostRow();
        int length = hostRows.size();
        int i = 0;
        while (i < hostRows.size()) {
            if (hostRows.get(i).getLocation().indexOf(location) == -1) {
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        modelAndView.addObject("username", name);
        return modelAndView;
    }

    @RequestMapping(value = "/host")
    public ModelAndView hostsPage(@RequestParam("hostId") int hostId, @RequestParam(required = false, defaultValue = "hidden") String instMetrics, @RequestParam(required = false, defaultValue = "hidden") String problems, @RequestParam(required = false, defaultValue = "-1") int instMetricId, @RequestParam(required = false, defaultValue = "title") String title, @RequestParam(required = false, defaultValue = "0") String startDate, @RequestParam(required = false, defaultValue = "0") String endDate) throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hosts");
        modelAndView.addObject("getHosts", getHostRow());
        modelAndView.addObject("hostId", hostId);
        modelAndView.addObject("instMetrics", instMetrics);
        modelAndView.addObject("problems", problems);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        modelAndView.addObject("username", name);
        //metrics
        modelAndView.addObject("title", title);
        modelAndView.addObject("instMetricId", instMetricId);
        modelAndView.addObject("getMetrics", getMetricRow(hostId));

        //problems
        if (instMetricId > 0 & problems.equals("show")) {
            modelAndView.addObject("getMetricProblems", getMetricProblems(hostId, instMetricId));

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
        String mTimeZone = startDate.substring(20, 23);
        String mActualDate = startDate.replace(mTimeZone + " ", "");
        System.out.println(mActualDate);
        String TWITTER = "EEE MMM dd HH:mm:ss yyyy";
        SimpleDateFormat mSf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        mSf.setTimeZone(TimeZone.getDefault());
        Date mNewDate = mSf.parse(mActualDate);
        return mNewDate;
    }


    @RequestMapping(params = {"search", "hostName"}, value = "/hosts", method = RequestMethod.GET)
    public ModelAndView hostsPageSearchHostName(String hostName) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        List<hostRow> hostRows = new ArrayList<hostRow>();
        hostRows = getHostRow();
        int length = hostRows.size();
        int i = 0;
        while (i < hostRows.size()) {
            if (hostRows.get(i).getHostName().indexOf(hostName) == -1) {
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        modelAndView.addObject("username", name);
        return modelAndView;
    }


    //TODO:page addHost
    @RequestMapping(method = RequestMethod.GET, value = "addHostPage")
    public ModelAndView addHostPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addHost");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        modelAndView.addObject("username", name);
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


    //TODO Favorites

    @RequestMapping(value = "/addToFavorites", method = RequestMethod.GET)
    public String addFavorites(@RequestParam("hostId") int hostId, @RequestParam("instMetricId") int instMetricId, @RequestParam("title") String title, @RequestParam("user") String user) throws SQLException {
        metricStorage.addToFavorites(hostId, instMetricId,user);
        return "Метрика добавлена";
        //return "redirect:/host?hostId=" + hostId + "&instMetrics=show&instMetricId=" + instMetricId + "&title=" + title;
    }

    @RequestMapping(value = "/dellFromFavorites", method = RequestMethod.GET)
    public String dellFavorites(@RequestParam("favoritesId") int favoritesId) throws SQLException {
        metricStorage.dellFromFavorites(favoritesId);
        return "redirect:/";
    }

}
