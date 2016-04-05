package net.core.alarms.dao;

import net.core.alarms.GenericAlarm;
import net.core.configurations.SSHConfiguration;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class GenericAlarmService implements GenericAlarmDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void save(GenericAlarm content) {
        em.persist(content);
    }

    @Override
    public List<GenericAlarm> getAll() {
        return em.createQuery("from GenericAlarm", GenericAlarm.class).getResultList();
    }

    @Transactional
    @Override
    public void remove(GenericAlarm content) {
        em.remove(content);
    }

    @Transactional
    @Override
    public void update(GenericAlarm content) {
        em.find(GenericAlarm.class, content.getId());
        em.merge(content);
    }

    public GenericAlarm get(int id) {
        return em.createQuery("from GenericAlarm where id ="+id, GenericAlarm.class).getSingleResult();
    }
    public List<GenericAlarm> getByHost(int hostid) {
        List results = em.createQuery("from GenericAlarm where hostid ="+hostid, GenericAlarm.class).getResultList();
        return results;
    }
    public List<GenericAlarm> getByMetric(int serviceid) {
        List results = em.createQuery("from GenericAlarm where serviceid ="+serviceid, GenericAlarm.class).getResultList();
        return results;
    }
    public List<GenericAlarm> getByUser(String username) {
        List results = em.createQuery("from GenericAlarm where username ='"+username+"'", GenericAlarm.class).getResultList();
        return results;
    }

}
