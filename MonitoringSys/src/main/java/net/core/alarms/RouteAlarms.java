package net.core.alarms;

import net.core.alarms.dao.AlarmsLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("RouteAlarms")
public class RouteAlarms {


    @Autowired
    private AlarmsLogDao alarmsLogDao;

    private String message;

    private MailAgent mailAgent = new MailAgent();

    public RouteAlarms() {
    }


    public void sendMessage(String type,GenericAlarm genericAlarm,String message) {
        this.message=message;
        send(genericAlarm,type);

    }

    public void sendMessage(String type,GenericAlarm genericAlarm) {
        this.message=genericAlarm.getMessage();
        send(genericAlarm,type);
    }

    public void sendMessage(String type,List<GenericAlarm> genericAlarms,String message) {
        this.message=message;
        for (GenericAlarm genericAlarm : genericAlarms) {
            send(genericAlarm,type);
        }
    }
    public void sendMessage(String type,List<GenericAlarm> genericAlarms) {
        for (GenericAlarm genericAlarm : genericAlarms) {
            this.message=genericAlarm.getMessage();
            send(genericAlarm,type);
        }
    }

    private void send(GenericAlarm genericAlarm,String type) {
        if (genericAlarm.getToEmail() != null) {
            mailAgent.send(message, genericAlarm.getToEmail());
        }if (genericAlarm.getToUser() != null) {
            AlarmsLog alarmsLog= new AlarmsLog();
            alarmsLog.setMessage(this.message);
            alarmsLog.setTouser(genericAlarm.getToUser());
            alarmsLog.setType(type);
            alarmsLog.setViewed(false);
            alarmsLogDao.save(alarmsLog);
        }
    }
}
