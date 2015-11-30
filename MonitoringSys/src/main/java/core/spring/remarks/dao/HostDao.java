package core.spring.remarks.dao;


import core.configurations.SSHConfiguration;
import core.spring.remarks.models.Content;

import java.util.List;

public interface HostDao {

    public void save(SSHConfiguration sshConfiguration);
    public List<SSHConfiguration> getAll();

}
