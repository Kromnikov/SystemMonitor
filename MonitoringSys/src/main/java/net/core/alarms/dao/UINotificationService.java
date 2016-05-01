package net.core.alarms.dao;

import net.core.alarms.UINotification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UINotificationService implements UINotificationDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void save(UINotification content) {
        em.persist(content);
    }

    @Override
    public List<UINotification> getAll() {
        return em.createQuery("from UINotification", UINotification.class).getResultList();
    }

    @Transactional
    @Override
    public void remove(UINotification content) {
        em.remove(content);
    }

    @Transactional
    @Override
    public void update(UINotification content) {
        em.find(UINotification.class, content.getId());
        em.merge(content);
    }

    public UINotification get(int id) {
        return em.createQuery("from UINotification where id ="+id, UINotification.class).getSingleResult();
    }
    public List<UINotification> getByUser(String userName) {
        List results = em.createQuery("from UINotification where touser ='" + userName + "' and viewed = 'false'", UINotification.class).getResultList();
        return results;
    }
}
