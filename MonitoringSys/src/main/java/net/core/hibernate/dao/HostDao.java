package net.core.hibernate.dao;


import net.core.configurations.SSHConfiguration;

import java.util.List;

public interface HostDao {

    public void save(SSHConfiguration sshConfiguration);

    public List<SSHConfiguration> getAll();

    public void remove(SSHConfiguration content);

    public void update(SSHConfiguration content);

    public SSHConfiguration get(int id);

}
