package net.core.db.interfaces;


import net.core.models.AlarmRow;
import net.core.models.GenericAlarmsRow;

import java.util.List;

public interface IAlarmsStorage {

    public List<GenericAlarmsRow> getAlarms(String userName);

    public AlarmRow getAlarm(int id);

    public AlarmRow getNewAlarm();

    public void updateAlarm(int id, int serviseId, int hostId, String toEmail, String toUser);

    public void addAlarm(int serviseId, int hostId, String toEmail, String toUser,String user);

    public void dellAlarm(int id);
}
