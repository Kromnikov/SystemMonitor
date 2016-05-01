package net.core.db;

import net.core.alarms.dao.AlarmsLogDao;
import net.core.alarms.dao.GenericAlarmDao;
import net.core.hibernate.services.HostService;
import net.core.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UsersStorage implements IUsersStorage {
    private JdbcTemplate jdbcTemplateObject;
    @Autowired
    private AlarmsLogDao alarmsLogDao;
    @Autowired
    private HostService hosts;
    @Autowired
    private GenericAlarmDao genericAlarm;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public UsersStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }




    @Transactional
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        String sql = "SELECT u.username , u.password, r.role,r.id FROM \"Users\" as u, \"Roles\" as r where u.username=r.username";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        for (Map row : rows) {
            User user = new User();
            user.setUsername((String)row.get("username"));
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
        List<String> stringList = new ArrayList<>();
        for (Map row : rows) {
            stringList.add((String)row.get("role"));
        }

        return stringList;
    }

    @Transactional
    public User getUsers(String userName) {
        String sql = "SELECT u.username , u.password, r.role,r.id FROM \"Users\" as u join \"Roles\" as r on r.username=u.username where u.username = '"+userName+"'";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        User user = new User();
        for (Map row : rows) {
            user.setUsername((String)row.get("username"));
            user.setPassword((String)row.get("password"));
            user.setRole((String)row.get("role"));
            user.setRoleid((int)row.get("id"));
        }
        user.setAllRoles(allRoles());
        return user;
    }
    @Transactional
    private List<String> allRoles() {
        String sql = "SELECT distinct r.role FROM \"Roles\" as r ";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        List<String> stringList = new ArrayList<>();
        for (Map row : rows) {
            stringList.add((String)row.get("role"));
        }
        return stringList;
    }

    @Transactional
    public void updateUser(String username,String password,String role)  {
        updateRole(username,role);
        updateUser(username,password);
    }
    @Transactional
    private void updateRole(String username,String role) {
        String sql ="UPDATE \"Roles\"   SET role='"+role+"', username='"+username+"' WHERE username='"+username+"'";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    private void updateUser(String username,String password) {
        String  sql ="UPDATE \"Users\" SET username='"+username+"', password='"+password+"' WHERE username='"+username+"'";
        jdbcTemplateObject.update(sql);
    }


    @Transactional
    public void addUser(String username,String password,String role)  {
        insertUser(username,password);
        insertRole(username,role);
    }
    @Transactional
    private void insertRole(String username,String role) {
        String  sql ="INSERT INTO \"Roles\" (role, username)   VALUES (?,?)";
        jdbcTemplateObject.update(sql,role,username);
    }
    @Transactional
    private void insertUser(String username,String password) {
        String sql ="INSERT INTO  \"Users\" (username, password, enabled) VALUES (?,?,?)";
        jdbcTemplateObject.update(sql,username,password,true);
    }


    @Transactional
    public void dellUser(String username)  {
        dellRole(username);
        dellUsers(username);
    }
    @Transactional
    private void dellRole(String username) {
        String sql ="DELETE FROM \"Roles\" where username=?";
        jdbcTemplateObject.update(sql,username);
    }
    @Transactional
    private void dellUsers(String username) {
        String sql ="DELETE FROM \"Users\" where username=?";
        jdbcTemplateObject.update(sql,username);
    }

    @Transactional
    public long getCountRoles() {
        String sql = "select count(*) from \"Roles\" ";
        return (long)jdbcTemplateObject.queryForMap(sql).get("count");
    }

    @Transactional
    public void setNewUserRole(String username,int roleid) {
        String sql ="UPDATE \"Users\" set  roleid=? WHERE username=?";
        jdbcTemplateObject.update(sql,roleid,username);
    }
}
