import net.core.IStorageServices;
import net.core.models.HostsState;
import net.web.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
         Application.class})
@ActiveProfiles("dev")

public class StorageServicesJUnitTest extends Assert{

    @Autowired
    private IStorageServices metricStorage;

    @Test
    public void testTransferService() throws SQLException, ParseException {
        List<HostsState> list =  metricStorage.getHostsProblems();
        assertEquals(0,list);
    }
}
