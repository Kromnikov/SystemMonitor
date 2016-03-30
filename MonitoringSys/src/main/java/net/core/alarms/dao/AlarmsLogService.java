package net.core.alarms.dao;

import net.core.alarms.AlarmsLog;
import net.core.alarms.GenericAlarm;
import net.core.configurations.SSHConfiguration;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class AlarmsLogService implements AlarmsLogDao{

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void save(AlarmsLog content) {
        em.persist(content);
    }

    @Override
    public List<AlarmsLog> getAll() {
        return em.createQuery("from AlarmsLog", AlarmsLog.class).getResultList();
    }

    @Transactional
    @Override
    public void remove(AlarmsLog content) {
        em.remove(content);
    }

    @Transactional
    @Override
    public void update(AlarmsLog content) {
        em.find(AlarmsLog.class, content.getId());
        em.merge(content);
    }

    public AlarmsLog get(int id) {
        return em.createQuery("from AlarmsLog where id ="+id, AlarmsLog.class).getSingleResult();
    }
    public List<AlarmsLog> getByUser(String userName) {
        List results = em.createQuery("from AlarmsLog where touser ='" + userName + "' and viewed = 'false'", AlarmsLog.class).getResultList();
        return results;
    }
}
