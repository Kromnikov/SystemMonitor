package net.core.db;

import net.core.db.interfaces.IInstanceStorage;
import net.core.db.interfaces.ITemplateStorage;
import net.core.models.InstanceMetric;
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
public class InstanceStorage implements IInstanceStorage{
    @Autowired
    private ITemplateStorage templateStorage;

    private JdbcTemplate jdbcTemplateObject;
    @Autowired
    public InstanceStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Transactional
    public void addInstMetric(int host, int metricId) {
        TemplateMetric templateMetric = templateStorage.getTemplateMetric(metricId);
        String sql = "INSERT INTO \"INSTANCE_METRIC\"(host, templ_metric,min_value,max_value,title,query) VALUES (?,?,?,?,?,?)";
        jdbcTemplateObject.update(sql, host, metricId, 0, 0, templateMetric.getTitle(), templateMetric.getCommand());
    }

    private InstanceMetric mapToInst(Map row) {
        InstanceMetric instanceMetric = new InstanceMetric();
        instanceMetric.setId((int) row.get("id"));
//        instanceMetric.setHostId(hostId);
        instanceMetric.setHostId((int) row.get("host"));
        instanceMetric.setTempMetrcId((int) row.get("templ_metric"));
        instanceMetric.setMinValue((double) row.get("min_value"));
        instanceMetric.setMaxValue((double) row.get("max_value"));
        instanceMetric.setCommand((String) row.get("query"));
        instanceMetric.setTitle((String) row.get("title"));
        return instanceMetric;
    }

    @Transactional
    public List<InstanceMetric> getInstMetrics(int hostId)  {
        String sql = "SELECT *  FROM \"INSTANCE_METRIC\" where host =?";
        return jdbcTemplateObject.queryForList(sql,hostId)
                .stream()
                .map(item->mapToInst(item))
                .collect(Collectors.toList());
    }

    @Transactional
    public InstanceMetric getInstMetric(int instMetricId)  {//TODO: надо что-то придумать, например каскадное удаление забыли
        String sql = "SELECT *  FROM \"INSTANCE_METRIC\" where id =?";
        try {
            return mapToInst(jdbcTemplateObject.queryForMap(sql, instMetricId));
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {

        }
        return new InstanceMetric();
    }

    @Transactional
    public void delMetricFromHost(int host, int id) {
        String sql = "delete from  \"INSTANCE_METRIC\" where id=? and host=?";
        jdbcTemplateObject.update(sql,id,host);
    }

    @Transactional
    public void addInstMetric(InstanceMetric instanceMetric) {
        String sql = "INSERT INTO \"INSTANCE_METRIC\"(host, templ_metric,min_value,max_value,title,query) VALUES (?,?,?,?,?,?)";
        jdbcTemplateObject.update(sql,instanceMetric.getHostId(),instanceMetric.getTempMetrcId(),instanceMetric.getMinValue(),instanceMetric.getMaxValue() , instanceMetric.getTitle() , instanceMetric.getCommand());
    }
    @Transactional
    public void editInstMetric(int id,int hostId,int templMetricId,String title,String command,double minValue,double maxValue)  {
        String sql = "UPDATE \"INSTANCE_METRIC\" SET min_value=?, host=?, templ_metric=?,max_value=?,title=?,query=? WHERE id=?";
        jdbcTemplateObject.update(sql,minValue,hostId,templMetricId,maxValue,title,command,id);
    }
}
