package net.core.models;

public class AlarmsModel {
    int id;
    String type;
    String message;

    public AlarmsModel() {
    }

    public AlarmsModel(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public AlarmsModel(String type, String message,int id) {
        this.id = id;
        this.type = type;
        this.message = message;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
