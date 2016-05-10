package net.core.db.interfaces;

import net.core.models.User;

import java.util.List;

public interface IUsersStorage {

    public List<User> getAllUsers();

    public List<String> getRoles();

    public User getUsers(String userName);

    public void updateUser(String username, String password, String role) ;

    public void addUser(String username, String password, String role) ;

    public void dellUser(String username);

    public long getCountRoles();

    public void setNewUserRole(String username, int roleid);
}
