package net.core.db;


import net.core.alarms.dao.AlarmsLogDao;
import net.core.alarms.dao.GenericAlarmDao;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class HostsStateStorage implements IHostsStateStorage{
    private JdbcTemplate jdbcTemplateObject;
    @Autowired
    private AlarmsLogDao alarmsLogDao;
    @Autowired
    private HostService hosts;
    @Autowired
    private GenericAlarmDao genericAlarm;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public HostsStateStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Transactional
    public boolean availableHost(long hostId) {//Нужен запрос на вывод состояния хоста
        String sql = "SELECT id, resolved, start_datetime, \"end_datetime\", host  FROM \"HOST_STATE\" where host = ? and \"end_datetime\" is null";
        boolean state = true;
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql,hostId);
        if (rows.isEmpty()) {
            return true;
        } else {
            for (Map row : rows) {
                state = (boolean) row.get("resolved");
            }
        }
        return state;
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
    @Transactional
    public List<HostsState> getHostsProblems() throws ParseException {
        List<HostsState> hostsStateList = new ArrayList<>();
        String sql = "SELECT id, resolved, start_datetime, end_datetime, host,host_name  FROM \"HOST_STATE\" where resolved = false";
        List<Map<String, Object>> rows = jdbcTemplateObject.queryForList(sql);
        if (rows.isEmpty()) {
            return hostsStateList;
        } else {
            int i = 0;
            for (Map row : rows) {
                HostsState hostStateTmp = new HostsState();
                hostStateTmp.setId(Integer.parseInt(row.get("id").toString()));
                hostStateTmp.setStart(dateFormat.parse(row.get("start_datetime").toString()));
                if (row.get("end_datetime") != null) {
                    hostStateTmp.setEnd(dateFormat.parse(row.get("end_datetime").toString()));
                }
                hostStateTmp.setResolved(Boolean.parseBoolean(row.get("resolved").toString()));
                hostStateTmp.setHostId(Integer.parseInt(row.get("host").toString()));

                hostStateTmp.setHostName(hosts.get(hostStateTmp.getHostId()).getName());
                i++;
                hostsStateList.add(hostStateTmp);
            }
        }
        return hostsStateList;
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
        return (long) jdbcTemplateObject.queryForMap(sql).get("COUNT");
    }

}
