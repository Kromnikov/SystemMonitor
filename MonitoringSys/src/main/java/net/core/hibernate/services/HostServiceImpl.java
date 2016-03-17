package net.core.hibernate.services;

import net.core.configurations.SSHConfiguration;
import net.core.hibernate.dao.HostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("HostService")
public class HostServiceImpl implements HostService {

    @Autowired
    private HostDao dao;

    @Transactional
    @Override
    public void save(SSHConfiguration sshConfiguration) {
        dao.save(sshConfiguration);
    }

    @Override
    public List<SSHConfiguration> getAll() {
        return dao.getAll();
    }

    @Transactional
    public void remove(SSHConfiguration content)
    {
        dao.remove(content);
    }

    @Transactional
    public void update(SSHConfiguration content)
    {
        dao.update(content);
    }

    public SSHConfiguration get(int id) {
        return dao.get(id);
    }

    @Override
    public List<SSHConfiguration> getByLocation(String location) {
        return dao.getByLocation(location);
    }

}
