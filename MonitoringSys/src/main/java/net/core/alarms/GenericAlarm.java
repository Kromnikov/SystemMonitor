package net.core.alarms;

import javax.persistence.*;

@Entity
@Table(name = "GenericAlarm")
public class GenericAlarm {
    @Id
    @Column(name = "id", updatable=false, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type")
    private String type;

    @Column(name = "serviceid",updatable=false, nullable=true)
    private Integer serviceId;

    @Column(name = "hostid",updatable=false, nullable=true)
    private Integer hostId;

    @Column(name = "toemail",updatable=false, nullable=true)
    private String toEmail;

    @Column(name = "touser",updatable=false, nullable=true)
    private String toUser;

    @Column(name = "message", nullable=true)
    private String message;

    @Column(name = "username", nullable=true)
    private String username;

    public GenericAlarm() {
    }

    public GenericAlarm(String type, int serviceId, int hostId, String toEmail, String toUser, String message) {
        this.type = type;
        this.serviceId = serviceId;
        this.hostId = hostId;
        this.toEmail = toEmail;
        this.toUser = toUser;
        this.message= message;
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

    public int getServiceId() {
        if(serviceId!=null)
            return serviceId;
        else
            return -1;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getHostId() {
        if(hostId!=null)
            return hostId;
        else
            return -1;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
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

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
