package net.core.db.interfaces;


import net.core.models.HostsState;

import java.text.ParseException;
import java.util.List;

public interface IHostsStateStorage {

    public boolean availableHost(long hostId);

    public void setNotAvailableHost(String startTime, int host, String hostName);

    public void setAvailableHost(String endTime, int host);

    public List<HostsState> getHostsProblems() throws ParseException;

    public void setResolvedHost(int id);

    public void setResolvedHost();

    public long getHostNotResolvedLength();
}
