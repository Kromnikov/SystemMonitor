package net.core.db;

import net.core.models.User;

import java.util.List;

/**
 * Created by Kromnikov on 01.05.2016.
 */
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
