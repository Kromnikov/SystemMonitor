package net.web.controller;

import net.core.IStorageController;
import net.core.hibernate.services.HostService;
import net.core.models.HostsState;
import net.core.models.MetricProblem;
import net.core.models.ProblemRow;
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
import java.util.List;

/**
 * Created by ANTON on 27.03.2016.
 */
@Controller
public class ProblemsController {
    @Autowired
    private IStorageController metricStorage;
    @Autowired
    private HostService hosts;

    public int getAllProblemsCount() throws SQLException {
        return ((int) metricStorage.getMetricNotResolvedLength() + (int) metricStorage.getHostNotResolvedLength());
    }

    public List<MetricProblem> getMetricProblems() throws SQLException, ParseException {
        return metricStorage.getMetricProblems();
    }

    public int getAllMetricProblemsCount() throws SQLException {
        return (int) metricStorage.getMetricNotResolvedLength();
    }

    public int getHostsProblemsCount() throws SQLException {
        return (int) metricStorage.getHostNotResolvedLength();
    }

    public List<HostsState> getHostsProblems() throws SQLException, ParseException {
        return metricStorage.getHostsProblems();
    }
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        modelAndView.addObject("username", name);
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

    @RequestMapping(value = "/hosts/resolve/metric", method = RequestMethod.GET)
    public String resolveMetricHostsPage(@RequestParam("resMetrId") int resMetrId,@RequestParam("hostId") int hostId,@RequestParam("instMetricId") int instMetricId) {
        metricStorage.setResolvedMetric(resMetrId);
        return "redirect:/host?hostId="+hostId+"&problems=show&instMetricId="+instMetricId;
    }

    @RequestMapping(value = "/problems/resolve/metric/all", method = RequestMethod.GET)
    public String resolveMetric() {
        metricStorage.setResolvedMetric();
        return "redirect:/problems";
    }

    @RequestMapping(value = "/problem/metric", method = RequestMethod.GET)
    public String redirectToMetric(@RequestParam("problemId") int problemId,@RequestParam(required=false,defaultValue = "0") String startDate,@RequestParam(required=false,defaultValue = "0") String endDate) throws SQLException {
        ProblemRow problemRow = metricStorage.getProblem(problemId);
        return "redirect:/host?hostId=" + problemRow.getHostId() + "&instMetrics=show&instMetricId=" + problemRow.getInstMetricId() + "&title=" + problemRow.getInstMetric()+"&startDate="+startDate+"&endDate="+endDate;
    }

}
