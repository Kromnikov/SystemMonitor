package net.core.db;

import net.core.alarms.GenericAlarm;
import net.core.alarms.dao.GenericAlarmDao;
import net.core.configurations.SSHConfiguration;
import net.core.db.interfaces.IAlarmsStorage;
import net.core.db.interfaces.IInstanceStorage;
import net.core.db.interfaces.IUsersStorage;
import net.core.hibernate.services.HostService;
import net.core.models.AlarmRow;
import net.core.models.GenericAlarmsRow;
import net.core.models.InstanceMetric;
import net.core.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AlarmsStorage implements IAlarmsStorage {
    @Autowired
    private IUsersStorage usersStorage;
    @Autowired
    private IInstanceStorage instanceStorage;

    private JdbcTemplate jdbcTemplateObject;
    @Autowired
    private HostService hosts;
    @Autowired
    private GenericAlarmDao genericAlarm;
    @Autowired
    public AlarmsStorage(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Transactional
    public List<GenericAlarmsRow> getAlarms(String userName) {
        List<GenericAlarmsRow> genericAlarmsRowList = new ArrayList<>();
        for (GenericAlarm ga : genericAlarm.getByUser(userName)) {
            GenericAlarmsRow genericAlarmsRow = new GenericAlarmsRow();
            genericAlarmsRow.setType(ga.getType());
            genericAlarmsRow.setMessage(ga.getMessage());
            if(ga.getHostId()>-1) {
                genericAlarmsRow.setHostId(ga.getHostId());
                genericAlarmsRow.setHostName(hosts.get(genericAlarmsRow.getHostId()).getName());
            }
            genericAlarmsRow.setId(ga.getId());
            {
                if (ga.getServiceId() > -1);
            }
            genericAlarmsRow.setServiceId(ga.getServiceId());
            genericAlarmsRow.setToEmail(ga.getToEmail());
            genericAlarmsRow.setToUser(ga.getToUser());
            genericAlarmsRow.setUser(ga.getUsername());

            InstanceMetric instanceMetric = instanceStorage.getInstMetric(genericAlarmsRow.getServiceId());
            genericAlarmsRow.setServiceTitle(instanceMetric.getTitle());
            genericAlarmsRow.setFromHost(hosts.get(instanceMetric.getHostId()).getName());


            genericAlarmsRowList.add(genericAlarmsRow);
        }
        return genericAlarmsRowList;
    }

    @Transactional
    public AlarmRow getAlarm(int id)  {
//        String sql = "";
//        List<Map<String, Object>> rows;
        GenericAlarm ga = genericAlarm.get(id);

        SSHConfiguration host = new SSHConfiguration();
        AlarmRow alarmRow = new AlarmRow();
        alarmRow.setType(ga.getType());
        alarmRow.setMessage(ga.getMessage());
        if (ga.getHostId() > -1) {
            alarmRow.setHostId(ga.getHostId());
            host = hosts.get(alarmRow.getHostId());
            alarmRow.setHostName(host.getName());
        }
        alarmRow.setId(ga.getId());
        {
            if (ga.getServiceId() > -1) ;
        }
        alarmRow.setServiceId(ga.getServiceId());
        alarmRow.setToEmail(ga.getToEmail());
        alarmRow.setToUser(ga.getToUser());
        alarmRow.setUser(ga.getUsername());


//        sql = "select title,host from \"INSTANCE_METRIC\" WHERE id=?";
//        rows = jdbcTemplateObject.queryForList(sql,alarmRow.getServiceId());
//        for (Map row : rows) {
//            alarmRow.setServiceTitle((String) row.get("title"));
//            alarmRow.setFromHost(hosts.get((int) row.get("host")).getName());
//        }
        InstanceMetric instanceMetric = instanceStorage.getInstMetric(alarmRow.getServiceId());
        alarmRow.setServiceTitle(instanceMetric.getTitle());
        alarmRow.setFromHost(hosts.get(instanceMetric.getHostId()).getName());

//        alarmRow.setInstanceMetrics(getInstMetrics(host.getId()));
        alarmRow.setInstanceMetrics(instanceStorage.getInstMetrics(host.getId()));
        alarmRow.setHosts(hosts.getAll());


        List<User> users = usersStorage.getAllUsers();
        List<String> stringList = new ArrayList<>();
        for (User user : users) {
            stringList.add(user.getUsername());
        }

//        sql = "SELECT username FROM \"Users\"";
//        rows = jdbcTemplateObject.queryForList(sql);
//        List<String> stringList = new ArrayList<>();
//        for (Map row : rows) {
//            stringList.add((String)row.get("username"));
//        }
        alarmRow.setAllUsers(stringList);

        return alarmRow;
    }

    @Transactional
    public AlarmRow getNewAlarm()  {
//        String sql = "";
//        List<Map<String, Object>> rows;

        SSHConfiguration host = new SSHConfiguration();
        AlarmRow alarmRow = new AlarmRow();


        InstanceMetric instanceMetric = instanceStorage.getInstMetric(alarmRow.getServiceId());
        alarmRow.setServiceTitle(instanceMetric.getTitle());
        alarmRow.setFromHost(hosts.get(instanceMetric.getHostId()).getName());
//        sql = "select title,host from \"INSTANCE_METRIC\" WHERE id=?";
//        rows = jdbcTemplateObject.queryForList(sql,alarmRow.getServiceId());
//        for (Map row : rows) {
//            alarmRow.setServiceTitle((String) row.get("title"));
//            alarmRow.setFromHost(hosts.get((int) row.get("host")).getName());
//        }

//        alarmRow.setInstanceMetrics(getInstMetrics(host.getId()));
        alarmRow.setInstanceMetrics(instanceStorage.getInstMetrics(host.getId()));
        alarmRow.setHosts(hosts.getAll());


        List<User> users = usersStorage.getAllUsers();
        List<String> stringList = new ArrayList<>();
        for (User user : users) {
            stringList.add(user.getUsername());
        }
//        sql = "SELECT username FROM \"Users\"";
//        rows = jdbcTemplateObject.queryForList(sql);
//        List<String> stringList = new ArrayList<>();
//        for (Map row : rows) {
//            stringList.add((String)row.get("username"));
//        }
        alarmRow.setAllUsers(stringList);

        return alarmRow;
    }

    @Transactional
    public void updateAlarm(int id, int serviseId, int hostId, String toEmail, String toUser) {
        String sql = "UPDATE genericalarm SET serviceid=?, hostid=?, toemail=?, touser=? WHERE id=?";
        jdbcTemplateObject.update(sql,serviseId,hostId,toEmail,toUser,id);
    }
    @Transactional
    public void addAlarm(int serviseId, int hostId, String toEmail, String toUser,String user) {
        String sql = "INSERT INTO genericalarm (serviceid, hostid, toemail, touser,  username)    VALUES (?,?,?,?,?)";
        jdbcTemplateObject.update(sql,serviseId,hostId,toEmail,toUser,user);
    }
    @Transactional
    public void dellAlarm(int id) {
        String sql = "DELETE FROM genericalarm WHERE id=?";
        jdbcTemplateObject.update(sql,id);
    }

}
