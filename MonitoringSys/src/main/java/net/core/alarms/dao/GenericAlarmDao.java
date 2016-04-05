package net.core.alarms.dao;

import net.core.alarms.GenericAlarm;

import java.util.List;

public interface GenericAlarmDao {
    public void save(GenericAlarm genericAlarm);

    public List<GenericAlarm> getAll();

    public void remove(GenericAlarm genericAlarm);

    public void update(GenericAlarm genericAlarm);

    public GenericAlarm get(int id);

    public List<GenericAlarm> getByHost(int hostid);

    public List<GenericAlarm> getByMetric(int serviceid);

    public List<GenericAlarm> getByUser(String username);
}
