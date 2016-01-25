package net.core.configurations;

import javax.persistence.*;

@Entity
@Table(name = "SSHConfigurationHibernate")
public class SSHConfiguration {
    @Id
    @Column(name = "SSHConfigurationHibernate_ID", updatable=false, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "host")
    private String host;

    @Column(name = "port")
    private int port;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    public SSHConfiguration() {

    }

    public SSHConfiguration(String host, int port, String login, String password) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
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
}
