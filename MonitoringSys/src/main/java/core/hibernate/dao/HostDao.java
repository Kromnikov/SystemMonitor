package core.hibernate.dao;


import core.configurations.SSHConfiguration;

import java.util.List;

public interface HostDao {

    public void save(SSHConfiguration sshConfiguration);

    public List<SSHConfiguration> getAll();

    public void remove(SSHConfiguration content);

}
