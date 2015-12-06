import core.SpringService;
import core.agents.SSHAgent;
import core.configurations.SSHConfiguration;
import core.hibernate.services.HostService;
import core.interfaces.db.IMetricStorage;
import core.models.Metric;

import java.sql.SQLException;

public class Program {

//    private static final Logger logger = Logger.getLogger(Program.class);

    public static void main(String[] args) throws SQLException {


		SpringService.run();

		IMetricStorage metricStorage =SpringService.getMetricStorage();
		HostService hosts = SpringService.getHosts();
		SSHAgent sshAgent;

		long start = System.currentTimeMillis();

		for (int i = 0; i < 2; i++) {
			for (SSHConfiguration host : hosts.getAll()) {//где-то пол секунды
				sshAgent = new SSHAgent(host);
				if (sshAgent.connect()) {
					for (Metric metric : metricStorage.getMetricsByHostId(host.getId())) {
//						sshAgent.getMetricValue(metric) самая долгая
						System.out.println(host.getId() + "////" + metric.getTitle() + ":" + metric.getId() + "////" + sshAgent.getMetricValue(metric));
					}
				}
			}
		}

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
