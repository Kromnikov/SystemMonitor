package net.core.models;

/**
 * Created by Kromnikov on 13.03.2016.
 */
public class hostRow {
    int id;
    String hostName;
    int servicesCount;
    int errorsCount;
    String status;

    public hostRow() {
    }

    public hostRow(int id, String hostName, int servicesCount, int errorsCount, String status) {
        this.id = id;
        this.hostName = hostName;
        this.servicesCount = servicesCount;
        this.errorsCount = errorsCount;
        this.status = status;
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
}
