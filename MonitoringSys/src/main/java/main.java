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
            IMetricStorage metricStorage = SpringService.getMetricStorage();
            List<String> hosts;
            hosts = metricStorage.getListIP();
            System.out.println(hosts.get(1));


//            SQLBranch.run();
//            long start = System.currentTimeMillis();
//
//            for (int i = 0; i < 10; i++) {
//                for (SSHConfiguration host : SQLBranch.getHosts()) {//�� ������
//                    sshAgent = new SSHAgent(host);
//                    if (sshAgent.connect()) {//����������� � �����
//                        for (Metric metric : SQLBranch.getMetricsByHostId(host.getId())) {//�� �������� �����
////                            SQLBranch.addValue(host.getId(), metric.getId(), sshAgent.getMetricValue(metric), dateFormat.format(new Date()));
//                              System.out.println(host.getId()+"////"+metric.getTitle()+":"+metric.getId()+"////"+sshAgent.getMetricValue(metric));
//                        }
//                    }
//                }
//
//            }
//            start = System.currentTimeMillis() - start;
//            System.out.println(start);
//            System.exit(0);




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

//            int j = 0;
            //List<SSHConfiguration> list;
            //list = SQLBranch.getHosts();
            //System.out.println(list.get(0));
            //Chart chart = new Chart();
//            j = SQLBranch.getQuantityOfRow(1);
//            System.out.println(j);



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
