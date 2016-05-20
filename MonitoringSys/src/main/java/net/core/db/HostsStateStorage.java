package net.core.db;


import net.core.db.interfaces.IHostsStateStorage;
import net.core.hibernate.services.HostService;
import net.core.models.HostsState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class HostsStateStorage implements IHostsStateStorage{
    private JdbcTemplate jdbcTemplateObject;
    @Autowired
    private HostService hosts;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public HostsStateStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Transactional
    public boolean availableHost(long hostId) {
        String sql = "SELECT COUNT(*)>0 FROM \"HOST_STATE\" where host = ? and \"end_datetime\" is null";
        return !jdbcTemplateObject.queryForObject(sql, Boolean.class, hostId);
    }
    @Transactional
    public void setNotAvailableHost(String startTime, int host, String hostName) {
        String sql = "INSERT INTO \"HOST_STATE\"(start_datetime,resolved,host,host_name)  VALUES ((TIMESTAMP '" + startTime + "'),?,?,?)";
        jdbcTemplateObject.update(sql,false,host,hostName);
    }
    @Transactional
    public void setAvailableHost(String endTime, int host) {
        String sql = "UPDATE \"HOST_STATE\" SET \"end_datetime\" = (TIMESTAMP '" + endTime + "')  where host =? and \"end_datetime\" is null";
        jdbcTemplateObject.update(sql,host);
    }

    private HostsState mapInHostState(Map row) {
        HostsState hostState = new HostsState();
        hostState.setId(Integer.parseInt(row.get("id").toString()));
        try {
            hostState.setStart(dateFormat.parse(row.get("start_datetime").toString()));
        if (row.get("end_datetime") != null) {
            hostState.setEnd(dateFormat.parse(row.get("end_datetime").toString()));
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        hostState.setResolved(Boolean.parseBoolean(row.get("resolved").toString()));
        hostState.setHostId(Integer.parseInt(row.get("host").toString()));

        hostState.setHostName(hosts.get(hostState.getHostId()).getName());
        return hostState;
    }

    @Transactional
    public List<HostsState> getHostsProblems() throws ParseException {
        String sql = "SELECT *  FROM \"HOST_STATE\" where resolved = false";
        return jdbcTemplateObject.queryForList(sql)
                .stream()
                .map(item-> mapInHostState(item))
                .collect(Collectors.toList());
    }
    @Transactional
    public void setResolvedHost(int id) {
        String sql = "UPDATE \"HOST_STATE\" set resolved = true WHERE id =? and \"end_datetime\" is not null";
        jdbcTemplateObject.update(sql,id);
    }
    @Transactional
    public void setResolvedHost() {
        String sql = "UPDATE \"HOST_STATE\" set resolved = true WHERE \"end_datetime\" is not null";
        jdbcTemplateObject.update(sql);
    }
    @Transactional
    public long getHostNotResolvedLength() {
        String sql = "SELECT COUNT(*)  FROM \"HOST_STATE\" where resolved = false";
        return jdbcTemplateObject.queryForObject(sql, Long.class);
    }

}
