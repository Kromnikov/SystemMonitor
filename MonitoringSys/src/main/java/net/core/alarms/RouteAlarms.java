package net.core.alarms;

import net.core.alarms.dao.UINotificationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("RouteAlarms")
public class RouteAlarms {


    @Autowired
    private UINotificationDao UINotificationDao;

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
            UINotification UINotification = new UINotification();
            UINotification.setMessage(this.message);
            UINotification.setTouser(genericAlarm.getToUser());
            UINotification.setType(type);
            UINotification.setViewed(false);
            UINotificationDao.save(UINotification);
        }
    }
}
