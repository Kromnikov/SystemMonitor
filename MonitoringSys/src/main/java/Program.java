import core.SpringService;
import core.agents.SSHAgent;
import core.configurations.SSHConfiguration;
import core.hibernate.services.HostService;
import core.interfaces.db.IMetricStorage;
import core.models.Metric;
import core.models.Value;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Program {

//    private static final Logger logger = Logger.getLogger(Program.class);

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws SQLException {


		SpringService.run();

		IMetricStorage metricStorage =SpringService.getMetricStorage();
//		HostService hosts = SpringService.getHosts();
//		SSHAgent sshAgent;

		long start = System.currentTimeMillis();


//		metricStorage.setFalseStateHost(dateFormat.format(new Date()),1);
		System.out.println(metricStorage.getState(1));
		metricStorage.setTrueStateHost(dateFormat.format(new Date()), 1);
		System.out.println(metricStorage.getState(1));










//		for (int i = 0; i < 2; i++) {
//			for (SSHConfiguration host : hosts.getAll()) {//где-то пол секунды
//				sshAgent = new SSHAgent(host);
//				if (sshAgent.connect()) {
//					for (Metric metric : metricStorage.getMetricsByHostId(host.getId())) {
////						sshAgent.getMetricValue(metric) самая долгая
//						System.out.println(host.getId() + "////" + metric.getTitle() + ":" + metric.getId() + "////" + sshAgent.getMetricValue(metric));
//					}
//				}
//			}
//		}
//		SpringService.save(new SSHConfiguration("192.168.56.101",22,"kromnikov","12345"));
//		for (SSHConfiguration host : SpringService.getHosts().getAll()) {//где-то пол секунды
//			SpringService.remove(host);
//		}

//		List<Value> values = metricStorage.getValues(2,1);
//		for (Value item : values) {
//			System.out.println(item.getDateTime());
//		}

//		Metric metric = metricStorage.getMetric("getCPU");
//		System.out.println(metricStorage.getMetric(2).getTitle());

		start = System.currentTimeMillis() - start;
		System.out.println(start);
		System.exit(0);
	}

}
