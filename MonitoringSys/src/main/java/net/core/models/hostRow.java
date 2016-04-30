package net.core.models;

public class hostRow {

    int id;
    String hostName;
    int servicesCount;
    int errorsCount;
    String status;
    String location;

    public hostRow() {

    }


    public hostRow(int id, String hostName, int servicesCount, int errorsCount, String status, String location) {
        this.id = id;
        this.hostName = hostName;
        this.servicesCount = servicesCount;
        this.errorsCount = errorsCount;
        this.status = status;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getServicesCount() {
        return servicesCount;
    }

    public void setServicesCount(int servicesCount) {
        this.servicesCount = servicesCount;
    }

    public int getErrorsCount() {
        return errorsCount;
    }

    public void setErrorsCount(int errorsCount) {
        this.errorsCount = errorsCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
