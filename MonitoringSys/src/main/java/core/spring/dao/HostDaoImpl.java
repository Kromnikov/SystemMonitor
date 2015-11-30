package core.spring.dao;

import core.configurations.SSHConfiguration;
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
}
