package net.core.db;


import net.core.alarms.dao.AlarmsLogDao;
import net.core.alarms.dao.GenericAlarmDao;
import net.core.db.interfaces.IHomePageStorage;
import net.core.hibernate.services.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Repository
public class HomePageStorage implements IHomePageStorage {
    private JdbcTemplate jdbcTemplateObject;
    @Autowired
    private AlarmsLogDao alarmsLogDao;
    @Autowired
    private HostService hosts;
    @Autowired
    private GenericAlarmDao genericAlarm;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public HomePageStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Transactional
    public void addToFavorites(int host, int metric,String user) {
        String sql = "INSERT INTO \"FAVORITES\"(host_id,inst_metric_id,user_name) VALUES (?,?,?)";
        jdbcTemplateObject.update(sql,host,metric,user);
    }

    @Transactional
    public void dellFromFavorites(int favoritesId) {
        String sql = "DELETE FROM \"FAVORITES\" WHERE id = ?";
        jdbcTemplateObject.update(sql,favoritesId);
    }

    @Transactional
    public int hostsProblemsCount() {
        String sql = "SELECT count(*)  FROM \"HOST_STATE\"  where \"end_datetime\" is null";
        return Integer.parseInt(jdbcTemplateObject.queryForMap(sql).get("count").toString());
    }

    @Transactional
    public int hostsSuccesCount()  {
//        String sql = "SELECT count(*)  FROM \"INSTANCE_METRIC\"  where \"end_datetime\" is not null";
//        return Integer.parseInt(jdbcTemplateObject.queryForMap(sql).get("count").toString());
        return hosts.getAll().size();
    }
    @Transactional
    public int metricsProblemCount()  {
        String sql = "SELECT count(*)  FROM \"METRIC_STATE\"  where \"end_datetime\" is null";
        return Integer.parseInt(jdbcTemplateObject.queryForMap(sql).get("count").toString());
    }
    @Transactional
    public int metricsSuccesCount()  {//TODO стоит делать или нет, хз
        String sql = "SELECT count(*)  FROM \"INSTANCE_METRIC\"";
//        if (hostsSuccesCount() > 0) {
        return Integer.parseInt(jdbcTemplateObject.queryForMap(sql).get("count").toString());
//        }
//        return 0;
    }
}
