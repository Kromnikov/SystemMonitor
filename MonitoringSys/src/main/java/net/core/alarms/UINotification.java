package net.core.alarms;

import javax.persistence.*;

@Entity
@Table(name = "alarms")
public class UINotification {
    @Id
    @Column(name = "id", updatable=false, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "message", nullable=true)
    private String message;

    @Column(name = "touser",updatable=false, nullable=true)
    private String touser;

    @Column(name = "type")
    private String type;

    @Column(name = "viewed")
    private Boolean viewed;

    public UINotification() {
    }

    public UINotification(String message, String touser, String type, Boolean viewed) {
        this.message = message;
        this.touser = touser;
        this.type = type;
        this.viewed = viewed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getViewed() {
        return viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }
}
