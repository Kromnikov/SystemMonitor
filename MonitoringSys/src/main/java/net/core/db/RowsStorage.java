package net.core.db;

import net.core.configurations.SSHConfiguration;
import net.core.db.interfaces.IRowsStorage;
import net.core.hibernate.services.HostService;
import net.core.models.HostEditRow;
import net.core.models.HostRow;
import net.core.models.MetricRow;
import net.core.models.ProblemRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class RowsStorage implements IRowsStorage {
    private JdbcTemplate jdbcTemplateObject;
    @Autowired
    private HostService hosts;

    @Autowired
    public RowsStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Transactional
    public List<HostRow> getHostRow() {
        List<HostRow> hostrows = new ArrayList<>();
        String sql = "(select count(*) as countServices,host,(select count(*)from \"METRIC_STATE\" where host_id = im.host) as countProblems ,(select count(*)from \"HOST_STATE\" where host = im.host and (\"end_datetime\" is null and \"start_datetime\" is not null)) as status from \"INSTANCE_METRIC\" as im group by host)";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        for (SSHConfiguration host : this.hosts.getAll()) {
            HostRow hostRow = new HostRow();
            hostRow.setId(host.getId());
            hostRow.setHostName(host.getName());
            hostRow.setLocation(host.getLocation());
            for (Map row : rows) {
                if (host.getId() == (int) row.get("host")) {
                    hostRow.setServicesCount(Integer.parseInt(row.get("countServices").toString()));
                    hostRow.setErrorsCount(Integer.parseInt(row.get("countProblems").toString()));
                    hostRow.setStatus(row.get("status").toString());
                }
            }
            hostrows.add(hostRow);
        }
        return hostrows;
    }

    @Transactional
    public List<HostEditRow> getHostEditRow() {
        List<HostEditRow> hostrows = new ArrayList<>();
        String sql = "(select count(*) as countServices,host,(select count(*)from \"METRIC_STATE\" where host_id = im.host) as countProblems ,(select count(*)from \"HOST_STATE\" where host = im.host and (\"end_datetime\" is null and \"start_datetime\" is not null)) as status from \"INSTANCE_METRIC\" as im group by host)";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);

        for (SSHConfiguration host : this.hosts.getAll()) {
            HostEditRow hostRow = new HostEditRow();
            hostRow.setId(host.getId());
            hostRow.setName(host.getName());
            hostRow.setHost(host.getHost());
            hostRow.setPort(host.getPort());
            hostRow.setLogin(host.getLogin());
            hostRow.setPassword(host.getPassword());
            hostRow.setLocation(host.getLocation());
            for (Map row : rows) {
                if (host.getId() == (int) row.get("host")) {
                    hostRow.setServicesCount(Integer.parseInt(row.get("countServices").toString()));
                    hostRow.setErrorsCount(Integer.parseInt(row.get("countProblems").toString()));
                    hostRow.setStatus(row.get("status").toString());
                }
            }
            hostrows.add(hostRow);
        }
        return hostrows;
    }

    @Transactional
    public List<MetricRow> getMetricRow(int hostId) {
        String sql = "select id,title ,(select value from \"VALUE_METRIC\" where metric = im.id ORDER BY id DESC limit 1) as value ,(select date_time from \"VALUE_METRIC\" where metric = im.id ORDER BY id DESC limit 1) as date ,(select count(*)from \"METRIC_STATE\" where inst_metric = im.id ) as countProblems ,(select count(*)from \"METRIC_STATE\" where inst_metric = im.id and (\"end_datetime\" is null and \"start_datetime\" is not null)) as status  from \"INSTANCE_METRIC\" as im where host = ?";
        return jdbcTemplateObject.queryForList(sql, hostId)
                .stream()
                .map(item -> mapToMetricRow(item))
                .collect(Collectors.toList());
    }


    @Transactional
    public ProblemRow getProblem(int problemId) {
        String sql = "SELECT i.title , a.host_id, a.inst_metric,a.start_datetime,a.end_datetime FROM \"METRIC_STATE\" as a , \"INSTANCE_METRIC\" as i where a.id = ? and i.id = a.inst_metric";
        return mapToProblemRow(jdbcTemplateObject.queryForMap(sql, problemId));
    }



    private MetricRow mapToMetricRow(Map row) {
        MetricRow metricrow = new MetricRow();
        metricrow.setId(Integer.parseInt(row.get("id").toString()));
        metricrow.setTitle((row.get("title").toString()));
        metricrow.setErrorsCount(Integer.parseInt(row.get("countProblems").toString()));
        if (row.get("value") != null)
            metricrow.setLastValue(Double.parseDouble(row.get("value").toString()));
        metricrow.setDate(((java.sql.Timestamp) row.get("date")));
        metricrow.setStatus(row.get("status").toString());
        return metricrow;
    }


    private ProblemRow mapToProblemRow(Map row) {
        ProblemRow problemRow = new ProblemRow();
        problemRow.setHostId((int) row.get("host_id"));
        problemRow.setInstMetricId((int) row.get("inst_metric"));
        problemRow.setInstMetric((String) row.get("title"));
        problemRow.setStartDate(((java.sql.Timestamp) row.get("start_datetime")));
        problemRow.setEndDate(((java.sql.Timestamp) row.get("end_datetime")));
        return problemRow;
    }
}
