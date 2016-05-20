package net.core.db;

import net.core.db.interfaces.ITemplateStorage;
import net.core.models.TemplateMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TemplateStorage implements ITemplateStorage{

    private JdbcTemplate jdbcTemplateObject;
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
        String sql = "select * FROM \"TEMPLATE_METRICS\" where id =?";
        return mapToTempl(jdbcTemplateObject.queryForMap(sql, id));
    }

    @Transactional
    public List<TemplateMetric> getTemplatMetrics()  {
        String sql = "SELECT * FROM \"TEMPLATE_METRICS\"";
        return jdbcTemplateObject.queryForList(sql)
                .stream()
                .map(item-> mapToTempl(item))
                .collect(Collectors.toList());
    }

    private TemplateMetric mapToTempl(Map row) {
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
