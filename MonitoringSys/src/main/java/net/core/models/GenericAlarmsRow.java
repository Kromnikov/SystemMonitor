package net.core.models;

public class GenericAlarmsRow {
    private int id;

    private String type;

    private Integer serviceId;
    private String serviceTitle;
    private String fromHost;

    private Integer hostId;
    private String hostName;

    private String toEmail;

    private String toUser;

    private String message;

    private String user;

    public GenericAlarmsRow() {
    }

    public GenericAlarmsRow(int id, String type, Integer serviceId, String serviceTitle, Integer hostId, String hostName, String toEmail, String toUser, String message, String user) {
        this.id = id;
        this.type = type;
        this.serviceId = serviceId;
        this.serviceTitle = serviceTitle;
        this.hostId = hostId;
        this.hostName = hostName;
        this.toEmail = toEmail;
        this.toUser = toUser;
        this.message = message;
        this.user = user;
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

    public String getFromHost() {
        return fromHost;
    }

    public void setFromHost(String fromHost) {
        this.fromHost = fromHost;
    }
}
