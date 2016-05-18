package net.core.db;

import net.core.alarms.dao.AlarmsLogDao;
import net.core.alarms.dao.GenericAlarmDao;
import net.core.db.interfaces.ITemplateStorage;
import net.core.hibernate.services.HostService;
import net.core.models.TemplateMetric;
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
public class TemplateStorage implements ITemplateStorage{
    private JdbcTemplate jdbcTemplateObject;
    @Autowired
    private AlarmsLogDao alarmsLogDao;
    @Autowired
    private HostService hosts;
    @Autowired
    private GenericAlarmDao genericAlarm;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public TemplateStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Transactional
    public void addTemplateMetric(String title, String query) {
        String sql = "INSERT INTO \"TEMPLATE_METRICS\"(title, query) VALUES (?,?)";
        jdbcTemplateObject.update(sql,title,query);
    }

    @Transactional
    public TemplateMetric getTemplateMetric(int id) {
//        TemplateMetric templateMetric = new TemplateMetric();
        String sql = "select * FROM \"TEMPLATE_METRICS\" where id =?";
        Map<String, Object> row = jdbcTemplateObject.queryForMap(sql, id);
        TemplateMetric templateMetric = getTemplate(row);
//        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql,id);
//        for (Map row : rows) {
//            templateMetric = getTemplate(row);
//        }
        return templateMetric;
    }

    @Transactional
    public List<TemplateMetric> getTemplatMetrics()  {
        List<TemplateMetric> templateMetrics = new ArrayList<>();
        String sql = "SELECT * FROM \"TEMPLATE_METRICS\"";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        rows.stream().forEach(row -> templateMetrics.add(getTemplate(row)));
//        for (Map row : rows) {
//            templateMetrics.add(getTemplate(row));
//        }
        return templateMetrics;
    }

    private TemplateMetric getTemplate(Map row) {
        TemplateMetric templateMetric = new TemplateMetric();
        templateMetric.setId((int) row.get("id"));
        templateMetric.setTitle((String) row.get("title"));
        templateMetric.setCommand((String) row.get("query"));
        if(row.get("min_value")!=null)
            templateMetric.setMinValue((double) row.get("min_value"));
        if(row.get("max_value")!=null)
            templateMetric.setMaxValue((double) row.get("max_value"));
        return templateMetric;
    }

    @Transactional
    public void updateTemplMetric(int id,String title,String command,double minValue,double maxValue) {
        String sql = "UPDATE \"TEMPLATE_METRICS\" SET min_value=?,max_value=?,title=?,query=? WHERE id=?";
        jdbcTemplateObject.update(sql,minValue,maxValue,title,command,id);
    }

    @Transactional
    public void addTemplMetric(String title,String command,double minValue,double maxValue) {
        String sql = "INSERT INTO  \"TEMPLATE_METRICS\"( min_value, max_value,title, query) VALUES( ?,?,?,?)";
        jdbcTemplateObject.update(sql,minValue,maxValue,title,command);
    }

    @Transactional
    public void dellTemplMetric(int id)  {
        String sql = "DELETE FROM \"TEMPLATE_METRICS\" where id=?";
        jdbcTemplateObject.update(sql,id);
    }
}
