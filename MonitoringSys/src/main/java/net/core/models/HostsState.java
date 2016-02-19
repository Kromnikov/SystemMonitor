package net.core.models;

import java.util.Date;

public class HostsState {

    private int id;

    private boolean resolved;

    private String state;

    private Date start;

    private Date end;

    private int hostId;

    private String hostName;

    public HostsState() {
    }

    public HostsState(int id, boolean resolved, Date end, int hostId, Date start) {
        this.id = id;
        this.resolved = resolved;
        this.end = end;
        this.hostId = hostId;
        this.start = start;
    }

    public HostsState(int id, boolean resolved, String state, Date start, Date end, int hostId,String hostName) {
        this.id = id;
        this.resolved = resolved;
        this.state = state;
        this.start = start;
        this.end = end;
        this.hostId = hostId;
        this.hostId = hostId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
