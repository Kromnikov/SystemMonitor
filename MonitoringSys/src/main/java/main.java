import com.core.SpringService;
import com.core.agents.SSHAgent;
import org.hibernate.Session;
import org.hsqldb.HsqlException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class main {
    private static Session session;
    private static SSHAgent sshAgent;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void main(String args[]) throws Exception {

        try {
            SpringService.run();
        }catch (HsqlException sqlerr) {
            return;
        }
    }

}
