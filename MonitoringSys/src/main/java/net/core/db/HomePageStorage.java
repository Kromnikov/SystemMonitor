package net.core.db;


import net.core.alarms.dao.AlarmsLogDao;
import net.core.alarms.dao.GenericAlarmDao;
import net.core.db.interfaces.IHomePageStorage;
import net.core.hibernate.services.HostService;
import net.core.models.Favorites;
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
        return Integer.parseInt(jdbcTemplateObject.queryForMap(sql).get("count").toString());
    }

    @Transactional
    public List<Favorites> getFavoritesRow(String name) {
        List<Favorites> favoriteses = new ArrayList<>();
        String sql = "select *,(select title from \"INSTANCE_METRIC\" where id = f.inst_metric_id) as title  from \"FAVORITES\" as f where f.user_name='"+name+"'";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        for (Map row : rows) {
            Favorites favorite = new Favorites();
            favorite.setId((int) row.get("id"));
            favorite.setHostId(Integer.parseInt(row.get("host_id").toString()));
            favorite.setMetricId(Integer.parseInt(row.get("inst_metric_id").toString()));
            favorite.setTitle(row.get("title").toString());
            favoriteses.add(favorite);
        }
        return favoriteses;
    }
}
