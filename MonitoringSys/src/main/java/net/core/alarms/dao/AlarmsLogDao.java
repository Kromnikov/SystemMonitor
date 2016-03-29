package net.core.alarms.dao;

import net.core.alarms.AlarmsLog;

import java.util.List;

public interface AlarmsLogDao {

    public void save(AlarmsLog alarmsLog);

    public List<AlarmsLog> getAll();

    public void remove(AlarmsLog alarmsLog);

    public void update(AlarmsLog alarmsLog);

    public AlarmsLog get(int id);

    public List<AlarmsLog> getByUser(String userName);
}
