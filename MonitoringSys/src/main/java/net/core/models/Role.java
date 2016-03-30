package net.core.models;

import javax.persistence.*;

/**
 * Created by ANTON on 17.02.2016.
 */

public class Role {

    private long roleid;
    private String role;

    public long getRoleid() {
        return roleid;
    }

    public void setRoleid(long roleid) {
        this.roleid = roleid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
