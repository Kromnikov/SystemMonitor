import core.branches.CoreBranch;
import core.branches.SQLBranch;
import core.configurations.SSHConfiguration;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionDelegatorBaseImpl;
import org.hsqldb.HsqlException;

public class main {
    private static Session session;
    public static void main(String args[]) throws Exception {

        try {


//        HSQLDBConfiguration SqlConfig = new HSQLDBConfiguration();
//        if (SqlConfig.load()) {
//            System.out.println("ok!");
//        }

//        SQLConfiguration sql = new SQLConfiguration();
//        if(sql.load()) {
//            SQLAgent sqlAgent = new SQLAgent(sql.getStatement());
////            sqlAgent.addValue(1, 1, 1);
////            sqlAgent.addMetric("'getFreeDiskMb'","'df -m | grep sda | awk ''{print $4}'' '");
//            SSHMetric metric = sqlAgent.1getSSHMetric(7);
//            System.out.println(metric.getTitle());
//            System.out.println(metric.getCommand());
//        }




        CoreBranch.run();
        SQLBranch.addHost("192.168.56.101", 22, "anton", "1111");


//        Session session = HibernateUtil2.getSessionFactory().openSession();
////        session.beginTransaction();
////        session.save(new SSHConfigurationHibernate("192.168.56.0", 22, "kromnikov", "12345"));
////            session.save(new SSHConfigurationHibernate("192.168.56.101",22,"kromnikov","12345"));
////        session.getTransaction().commit();
//        SSHConfigurationHibernate surveyInSession = (SSHConfigurationHibernate) session.get(SSHConfigurationHibernate.class, 1);
//            List<SSHConfigurationHibernate> a = session.createCriteria(SSHConfigurationHibernate.class).list();
//        System.out.println(surveyInSession.getHost());
//        HibernateUtil2.shutdown();


//            HibernateUtil hibernateUtil = new HibernateUtil();
//            Session session = hibernateUtil.getSession();
//            session.save(new SSHConfigurationHibernate("192.168.56.102", 22, "kromnikov", "12345"));
//            session.save(new SSHConfigurationHibernate("192.168.56.0", 22, "kromnikov", "12345"));
//            session.save(new SSHConfigurationHibernate("192.176.23.11", 22, "kromnikov", "12345"));
//            session.beginTransaction().commit();
//            session.flush();
//            SSHConfigurationHibernate surveyInSession = (SSHConfigurationHibernate) session.get(SSHConfigurationHibernate.class, 2);
////        hibernateUtil.checkData("select * from 'sshconfigurationhibernate'");
//            session.close();
//            hibernateUtil.checkData("select * from 'sshconfigurationhibernate'");

//        } catch (SQLSyntaxErrorException sqlerr) {
//            System.out.println(sqlerr.fillInStackTrace());;
        }catch (HsqlException sqlerr) {
            return;
        }
    }

}
