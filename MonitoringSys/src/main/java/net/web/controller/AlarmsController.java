package net.web.controller;

import net.core.IStorageController;
import net.core.alarms.AlarmsLog;
import net.core.alarms.dao.AlarmsLogDao;
import net.core.models.AlarmRow;
import net.core.models.AlarmsModel;
import net.core.models.InstanceMetric;
import net.core.tools.Authorization;
import net.core.tools.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "/admin")
public class AlarmsController {
    @Autowired
private Authorization authentication;
    @Autowired
    private AlarmsLogDao alarmsLogDao;
    @Autowired
    private IStorageController metricStorage;

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hotfound");
        return modelAndView;
    }
    //TODO:  Контроллер для alarms
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


    @RequestMapping(value="/alarms")
    public ModelAndView alarms() throws SQLException {
        /*if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }*/
        ModelAndView modelAndView = new ModelAndView();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        modelAndView.addObject("getAlarms", metricStorage.getAlarms(name));
        modelAndView.setViewName("alarms");
        modelAndView.addObject("username", name);
        return modelAndView;
    }
    @RequestMapping(value = "/getAlarm", method = RequestMethod.GET)
    @ResponseBody
    public AlarmRow getAlarm(@RequestParam("id") int id) throws SQLException {
        /*if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }*/
        return metricStorage.getAlarm(id);
    }
    @RequestMapping(value = "/getNewAlarm", method = RequestMethod.GET)
    @ResponseBody
    public AlarmRow getNewAlarm() throws SQLException {
        /*if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }*/
        return metricStorage.getNewAlarm();
    }
    @RequestMapping(value = "/getInstanceMetrics", method = RequestMethod.GET)
    @ResponseBody
    public List<InstanceMetric> getInstanceMetrics(@RequestParam("hostId") int id) throws SQLException {
        /*if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }*/
        return metricStorage.getInstMetrics(id);
    }
    @RequestMapping(value = "/saveAlarm", method = RequestMethod.GET)
    @ResponseBody
    public void saveAlarm(@RequestParam("id") int id,@RequestParam("toEmail") String toEmail,@RequestParam("toUser") String toUser,@RequestParam("metricId") int metricId,@RequestParam("hostId") int hostId) throws SQLException {
        /*if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }*/
        metricStorage.updateAlarm(id,metricId,hostId,toEmail,toUser);
    }
    @RequestMapping(value = "/addAlarm", method = RequestMethod.GET)
    @ResponseBody
    public void addAlarm(@RequestParam("toEmail") String toEmail,@RequestParam("toUser") String toUser,@RequestParam("metricId") int metricId,@RequestParam("hostId") int hostId,@RequestParam("user") String user) throws SQLException {
        /*if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }*/
        metricStorage.addAlarm(metricId, hostId, toEmail, toUser, user);
    }
    @RequestMapping(value = "/dellAlarms", method = RequestMethod.GET)
    @ResponseBody
    public void dellAlarm(@RequestParam("id") int id) throws SQLException {
        /*if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }*/
        metricStorage.dellAlarm(id);
    }
}
