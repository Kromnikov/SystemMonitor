package net.core.db;

import net.core.db.interfaces.IUsersStorage;
import net.core.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UsersStorage implements IUsersStorage {
    private JdbcTemplate jdbcTemplateObject;

    @Autowired
    public UsersStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }


    @Transactional
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        String sql = "SELECT * FROM \"Users\" as u, \"Roles\" as r where u.username=r.username";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        for (Map row : rows) {
            User user = new User();
            user.setUsername((String) row.get("username"));
            user.setPassword((String) row.get("password"));
            user.setRole((String) row.get("role"));
            user.setRoleid((int) row.get("id"));
            usersList.add(user);
        }
        return usersList;
    }

    @Transactional
    public List<String> getRoles() {
        String sql = "SELECT distinct r.role FROM \"Roles\" as r ";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        return rows.stream().map(item -> (String) item.get("role")).collect(Collectors.toList());
    }

    @Transactional
    public User getUsers(String userName) {
        String sql = "SELECT * FROM \"Users\" as u join \"Roles\" as r on r.username=u.username where u.username = ?";
        Map<String, Object> row = jdbcTemplateObject.queryForMap(sql,userName);
        User user = new User();
        user.setUsername((String) row.get("username"));
        user.setPassword((String) row.get("password"));
        user.setRole((String) row.get("role"));
        user.setRoleid((int) row.get("id"));
        user.setAllRoles(getRoles());

        return user;
    }

    @Transactional
    public void updateUser(String username, String password, String role) {
        updateRole(username, role);
        updateUser(username, password);
    }

    @Transactional
    private void updateRole(String username, String role) {
        String sql = "UPDATE \"Roles\"   SET role=?, username=? WHERE username=?";
        jdbcTemplateObject.update(sql,role,username,username);
    }

    @Transactional
    private void updateUser(String username, String password) {
        String sql = "UPDATE \"Users\" SET username=?, password=? WHERE username=?";
        jdbcTemplateObject.update(sql,username,password,username);
    }


    @Transactional
    public void addUser(String username, String password, String role) {
        insertUser(username, password);
        insertRole(username, role);
    }

    @Transactional
    private void insertRole(String username, String role) {
        String sql = "INSERT INTO \"Roles\" (role, username)   VALUES (?,?)";
        jdbcTemplateObject.update(sql, role, username);
    }

    @Transactional
    private void insertUser(String username, String password) {
        String sql = "INSERT INTO  \"Users\" (username, password, enabled) VALUES (?,?,?)";
        jdbcTemplateObject.update(sql, username, password, true);
    }


    @Transactional
    public void dellUser(String username) {
        dellRole(username);
        dellUsers(username);
    }

    @Transactional
    private void dellRole(String username) {
        String sql = "DELETE FROM \"Roles\" where username=?";
        jdbcTemplateObject.update(sql, username);
    }

    @Transactional
    private void dellUsers(String username) {
        String sql = "DELETE FROM \"Users\" where username=?";
        jdbcTemplateObject.update(sql, username);
    }

    @Transactional
    public long getCountRoles() {
        String sql = "select count(*) from \"Roles\" ";
        return jdbcTemplateObject.queryForObject(sql, Long.class);
    }

    @Transactional
    public void setNewUserRole(String username, int roleid) {
        String sql = "UPDATE \"Users\" set  roleid=? WHERE username=?";
        jdbcTemplateObject.update(sql, roleid, username);
    }
}
