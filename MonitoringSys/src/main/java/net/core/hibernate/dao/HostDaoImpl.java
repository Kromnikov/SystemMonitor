package net.core.hibernate.dao;

import net.core.configurations.SSHConfiguration;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class HostDaoImpl implements HostDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(SSHConfiguration content) {
        em.persist(content);
    }

    @Override
    public List<SSHConfiguration> getAll() {
        return em.createQuery("from SSHConfiguration", SSHConfiguration.class).getResultList();
    }

    @Override
    public void remove(SSHConfiguration content) {
        em.remove(content);
    }

    @Override
    public void update(SSHConfiguration content) {
        em.find(SSHConfiguration.class, content.getId());
        em.merge(content);
    }

    public SSHConfiguration get(int id) {
        return em.getReference(SSHConfiguration.class, id);
    }

}
