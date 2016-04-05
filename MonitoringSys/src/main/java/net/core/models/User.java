package net.core.models;

/**
 * Created by ANTON on 17.02.2016.
 */
import javax.persistence.*;
import java.util.List;


public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private List<String> allRoles;
    private int roleid;
    public User(){}
    public User(String name, String pass) {
        username = name;
        password = pass;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public List<String> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(List<String> allRoles) {
        this.allRoles = allRoles;
    }
}
