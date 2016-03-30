package net.core;

/**
 * Created by ANTON on 24.02.2016.
 */

import net.core.models.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * Created by ANTON on 24.02.2016.
 */

public class AccauntDb {

    private JdbcTemplate jdbcTemplateObject;

    @Autowired
    public AccauntDb(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Transactional
    public List<User> getAllUsers()
    {
        List<User> usersList = new ArrayList<>();
        String sql = "SELECT u.id,u.username , u.password, u.roleFROM \"Users\" as u. \"Roles\" as r where u.id=r.id";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        for (Map row : rows) {
            User user = new User();
            user.setId(Integer.parseInt((String) row.get("id")));
            user.setUsername((String)row.get("username"));
            user.setUsername((String)row.get("password"));
            user.setUsername((String)row.get("role"));
            usersList.add(user);
        }
        return usersList;
    }
}
