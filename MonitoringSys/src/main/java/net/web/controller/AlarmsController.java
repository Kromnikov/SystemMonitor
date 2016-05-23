package net.web.controller;

import net.core.IStorageController;
import net.core.models.AlarmRow;
import net.core.models.InstanceMetric;
import net.core.tools.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;


@Controller
@RequestMapping(value = "/admin")
public class AlarmsController {
    @Autowired
private Authorization authentication;
    @Autowired
    private IStorageController metricStorage;


    @RequestMapping(value="/alarms")
    public ModelAndView alarms() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        modelAndView.addObject("getGenericAlarmsRow", metricStorage.getGenericAlarmsRow(name));
        modelAndView.addObject("getAlarms", metricStorage.getAlarms());
        modelAndView.setViewName("alarms");
        modelAndView.addObject("username", name);
        return modelAndView;
    }
    @RequestMapping(value = "/getAlarm", method = RequestMethod.GET)
    @ResponseBody
    public AlarmRow getAlarm(@RequestParam("id") int id) throws SQLException {
        return metricStorage.getAlarm(id);
    }
    @RequestMapping(value = "/getNewAlarm", method = RequestMethod.GET)
    @ResponseBody
    public AlarmRow getNewAlarm() throws SQLException {
        return metricStorage.getNewAlarm();
    }
    @RequestMapping(value = "/getInstanceMetrics", method = RequestMethod.GET)
    @ResponseBody
    public List<InstanceMetric> getInstanceMetrics(@RequestParam("hostId") int id) throws SQLException {
        return metricStorage.getInstMetrics(id);
    }
    @RequestMapping(value = "/saveAlarm", method = RequestMethod.GET)
    @ResponseBody
    public void saveAlarm(@RequestParam("id") int id,@RequestParam("toEmail") String toEmail,@RequestParam("toUser") String toUser,@RequestParam("metricId") int metricId,@RequestParam("hostId") int hostId) throws SQLException {
        metricStorage.updateAlarm(id,metricId,hostId,toEmail,toUser);
    }
    @RequestMapping(value = "/addAlarm", method = RequestMethod.GET)
    @ResponseBody
    public void addAlarm(@RequestParam("toEmail") String toEmail,@RequestParam("toUser") String toUser,@RequestParam("metricId") int metricId,@RequestParam("hostId") int hostId,@RequestParam("user") String user) throws SQLException {
        metricStorage.addAlarm(metricId, hostId, toEmail, toUser, user);
    }
    @RequestMapping(value = "/dellAlarms", method = RequestMethod.GET)
    @ResponseBody
    public void dellAlarm(@RequestParam("id") int id) throws SQLException {
        metricStorage.dellAlarm(id);
    }
}
