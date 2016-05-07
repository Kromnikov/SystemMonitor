package net.web.controller;

import net.core.alarms.AlarmsLog;
import net.core.alarms.dao.AlarmsLogDao;
import net.core.models.AlarmsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class NotificationController {

    @Autowired
    private AlarmsLogDao alarmsLogDao;

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

}
