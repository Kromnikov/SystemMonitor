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
import java.util.stream.Collectors;

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
            if(ga.getHostId()>Integer.MIN_VALUE) {
                genericAlarmsRow.setHostId(ga.getHostId());
                genericAlarmsRow.setHostName(hosts.get(genericAlarmsRow.getHostId()).getName());
            }
            genericAlarmsRow.setId(ga.getId());
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
    public List<GenericAlarmsRow> getAlarms() {
        List<GenericAlarmsRow> genericAlarmsRowList = new ArrayList<>();
        for (GenericAlarm ga : genericAlarm.getAll()) {
            GenericAlarmsRow genericAlarmsRow = new GenericAlarmsRow();
            genericAlarmsRow.setType(ga.getType());
            genericAlarmsRow.setMessage(ga.getMessage());
            if(ga.getHostId()>Integer.MIN_VALUE) {
                genericAlarmsRow.setHostId(ga.getHostId());
                genericAlarmsRow.setHostName(hosts.get(genericAlarmsRow.getHostId()).getName());
            }
            genericAlarmsRow.setId(ga.getId());
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
        GenericAlarm ga = genericAlarm.get(id);

        SSHConfiguration host = new SSHConfiguration();
        AlarmRow alarmRow = new AlarmRow();
        alarmRow.setType(ga.getType());
        alarmRow.setMessage(ga.getMessage());
        if (ga.getHostId() > Integer.MIN_VALUE) {
            host = hosts.get(ga.getHostId());
            alarmRow.setHostId(host.getId());
            alarmRow.setHostName(host.getName());
            alarmRow.setInstanceMetrics(instanceStorage.getInstMetrics(host.getId()));
        }
        alarmRow.setId(ga.getId());
        alarmRow.setServiceId(ga.getServiceId());
        alarmRow.setToEmail(ga.getToEmail());
        alarmRow.setToUser(ga.getToUser());
        alarmRow.setUser(ga.getUsername());

        InstanceMetric instanceMetric = instanceStorage.getInstMetric(alarmRow.getServiceId());
        alarmRow.setServiceTitle(instanceMetric.getTitle());
        alarmRow.setFromHost(hosts.get(instanceMetric.getHostId()).getName());
        alarmRow.setHosts(hosts.getAll());

        alarmRow.setAllUsers(getAllUsers());

        return alarmRow;
    }


    @Transactional
    public AlarmRow getNewAlarm()  {
        AlarmRow alarmRow = new AlarmRow();
        alarmRow.setHosts(hosts.getAll());
        alarmRow.setAllUsers(getAllUsers());
        return alarmRow;
    }

    @Transactional
    public void updateAlarm(int id, int serviseId, int hostId, String toEmail, String toUser) {
        String sql = "UPDATE genericalarm SET serviceid=?, hostid=?, toemail=?, touser=? WHERE id=?";
        jdbcTemplateObject.update(sql,serviseId,hostId,toEmail,toUser,id);
    }
    @Transactional
    public void addAlarm(int serviseId, int hostId, String toEmail, String toUser,String user) {
        GenericAlarm genericAlarm1 = new GenericAlarm();
        genericAlarm1.setServiceId(serviseId);
        genericAlarm1.setHostId(hostId);
        genericAlarm1.setToEmail(toEmail);
        genericAlarm1.setToUser(toUser);
        genericAlarm1.setUsername(user);
        genericAlarm.save(genericAlarm1);
    }
    @Transactional
    public void dellAlarm(int id) {
//        String sql = "DELETE FROM genericalarm WHERE id=?";
//        jdbcTemplateObject.update(sql,id);
        genericAlarm.remove(genericAlarm.get(id));
    }


    private List<String> getAllUsers() {
        return usersStorage.getAllUsers().stream().map(item -> item.getUsername()).collect(Collectors.toList());
    }
}
