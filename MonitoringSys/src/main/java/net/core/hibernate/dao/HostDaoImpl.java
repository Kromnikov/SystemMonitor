package net.core.hibernate.dao;

import net.core.configurations.SSHConfiguration;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
        List<SSHConfiguration> sshConfiguration = em.createQuery("from SSHConfiguration where id ="+id, SSHConfiguration.class).getResultList();
//        SSHConfiguration sshConfiguration =  em.createQuery("from SSHConfiguration where id ="+id, SSHConfiguration.class).getSingleResult();
        if (sshConfiguration.size() > 0) {
            return sshConfiguration.get(0);
        } else {
            return new SSHConfiguration();
        }
//        return em.getReference(SSHConfiguration.class, id);
    }


    @Override
    public List<SSHConfiguration> getByLocation(String location) {
        TypedQuery<SSHConfiguration> query = em.createNamedQuery("SSHConfiguration.location", SSHConfiguration.class);
        query.setParameter("location", location);
        return query.getResultList();
    }


}
