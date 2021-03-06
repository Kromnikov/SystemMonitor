package net.core.configurations;

import javax.persistence.*;

@Entity
@Table(name = "SSHConfigurationHibernate")
public class SSHConfiguration {
    @Id
    @Column(name = "sshconfigurationhibernate_id", updatable=false, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "host")
    private String host;

    @Column(name = "port")
    private int port;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "location")
    private String location;

    public SSHConfiguration() {

    }

    public SSHConfiguration(String host, int port, String login, String password) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    public SSHConfiguration(String name, String host, int port, String login, String password) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    public SSHConfiguration(int id,String name, String host, int port, String login, String password, String location) {
        this.id=id;
        this.name = name;
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
        this.location = location;
    }

    public SSHConfiguration(String host) {
        this.id = id;
        this.host = host;
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
        return this.login;
    }

    public void setLogin(String login) {
        this.login=login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
