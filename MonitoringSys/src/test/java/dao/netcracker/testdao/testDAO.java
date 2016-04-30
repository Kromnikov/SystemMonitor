package dao.netcracker.testdao;

import net.core.MetricStorage;
import net.core.configurations.SSHConfiguration;
import net.core.hibernate.dao.HostDao;
import net.core.hibernate.dao.HostDaoImpl;
import net.core.hibernate.services.HostService;
import net.core.hibernate.services.HostServiceImpl;
import net.web.config.DatabaseConfig;
import org.hibernate.jpa.internal.EntityManagerImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.membermodification.MemberModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.mockito.Mockito.*;
/**
 * Created by ANTON on 28.03.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DatabaseConfig.class})
@WebAppConfiguration
@Transactional
public class TestDAO {
    private EmbeddedDatabase embeddedDatabase;

    private HostDaoImpl hostDao;

    @PersistenceContext
    private EntityManager em;

    @Before
    public void setUp() throws IllegalAccessException {

        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("DBScripts_create.sql")
                .addScript("inserts.sql")
                .build();
    }

    @Test
    public void getHostById() throws Exception {
        final int id = 1;
        SSHConfiguration host = new SSHConfiguration();
        host = hostDao.get(id);
    }


}
