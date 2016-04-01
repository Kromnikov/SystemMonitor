package net.core.models;

public class HostEditRow {
    int id;
    String name;
    String host;
    int port;
    String login;
    String password;
    String location;
    int servicesCount;
    int errorsCount;
    String status;

    public HostEditRow() {

    }

    public HostEditRow(int id, String name, String host, int port, String login, String password, String location, int servicesCount, int errorsCount, String status) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
        this.location = location;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
