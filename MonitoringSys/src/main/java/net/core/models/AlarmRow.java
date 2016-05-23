package net.core.models;

import net.core.configurations.SSHConfiguration;

import java.util.List;

public class AlarmRow {
    private int id;

    private String type;

    private Integer serviceId;
    private String serviceTitle;
    private String fromHost;
    private List<InstanceMetric> instanceMetrics;

    private Integer hostId;
    private String hostName;
    private List<SSHConfiguration> hosts;

    private String toEmail;

    private String toUser;

    private String message;

    private String user;

    private List<String> allUsers;

    public AlarmRow() {
    }

    public AlarmRow(List<String> allUsers,List<SSHConfiguration> hosts) {
        this.allUsers = allUsers;
        this.hosts = hosts;
    }

    public List<String> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<String> allUsers) {
        this.allUsers = allUsers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String getFromHost() {
        return fromHost;
    }

    public void setFromHost(String fromHost) {
        this.fromHost = fromHost;
    }

    public Integer getHostId() {
        return hostId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public List<SSHConfiguration> getHosts() {
        return hosts;
    }

    public void setHosts(List<SSHConfiguration> hosts) {
        this.hosts = hosts;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<InstanceMetric> getInstanceMetrics() {
        return instanceMetrics;
    }

    public void setInstanceMetrics(List<InstanceMetric> instanceMetrics) {
        this.instanceMetrics = instanceMetrics;
    }
}
