import core.branches.CoreBranch;
import core.branches.SQLBranch;
import org.hsqldb.HsqlException;

public class main {
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
