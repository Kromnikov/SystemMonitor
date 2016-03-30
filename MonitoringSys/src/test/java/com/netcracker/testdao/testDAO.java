package com.netcracker.testdao;

import net.core.MetricStorage;
import net.core.configurations.SSHConfiguration;
import net.core.hibernate.services.HostServiceImpl;
import net.web.config.DatabaseConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;
/**
 * Created by ANTON on 28.03.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DatabaseConfig.class})
@WebAppConfiguration
@Transactional
public class testDAO {
    @Test
    public void getHostById() throws Exception {
        final int id = 1;
        HostServiceImpl hostService = mock(HostServiceImpl.class);
        SSHConfiguration hosts = new SSHConfiguration();
        hostService.get(id);
        verify(hostService).get(1);
    }
    @Test
    public void getAllHost() throws Exception {
        final int id = 1;
        HostServiceImpl hostService = mock(HostServiceImpl.class);
        SSHConfiguration hosts = new SSHConfiguration();
        hostService.getAll();
        verify(hostService).getAll();
    }
    @Test
    public void addHost() throws Exception {
        HostServiceImpl hostService = mock(HostServiceImpl.class);
        SSHConfiguration hosts = new SSHConfiguration();
        hostService.save(hosts);
        verify(hostService).save(hosts);
    }
    @Test
    public void delHost() throws Exception {
        HostServiceImpl hostService = mock(HostServiceImpl.class);
        SSHConfiguration hosts = new SSHConfiguration();
        hostService.remove(hosts);
        verify(hostService).remove(hosts);
    }
    @Test
    public void findByLocationHost() throws Exception {
        HostServiceImpl hostService = mock(HostServiceImpl.class);
        SSHConfiguration hosts = new SSHConfiguration();
        hostService.getByLocation("");
        verify(hostService).getByLocation("");
    }

    @Test
    public void addStandartMetricTest() throws Exception{
        final int id=1;
        MetricStorage metricStorage = mock(MetricStorage.class);
        metricStorage.addStandartMetrics(id);
    }

    @Test
    public void addTempletMetric() throws Exception{
        MetricStorage metricStorage = mock(MetricStorage.class);
        metricStorage.addTemplateMetric("title","query");
    }

}
