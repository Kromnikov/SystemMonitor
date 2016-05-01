package net.core.alarms.dao;

import net.core.alarms.UINotification;

import java.util.List;

public interface UINotificationDao {

    public void save(UINotification UINotification);

    public List<UINotification> getAll();

    public void remove(UINotification UINotification);

    public void update(UINotification UINotification);

    public UINotification get(int id);

    public List<UINotification> getByUser(String userName);
}
