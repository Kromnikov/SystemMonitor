import core.SpringService;
import core.agents.SSHAgent;
import core.branches.CoreBranch;
import core.interfaces.db.IMetricStorage;
import org.hibernate.Session;
import org.hsqldb.HsqlException;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class main {
    private static Session session;
    private static SSHAgent sshAgent;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void main(String args[]) throws Exception {

        try {

            CoreBranch.run();
        }catch (HsqlException sqlerr) {
            return;
        }
    }

}
