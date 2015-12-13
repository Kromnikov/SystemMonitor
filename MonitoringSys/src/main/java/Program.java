import core.SpringService;
import core.configurations.SSHConfiguration;
import core.hibernate.services.HostService;
import core.interfaces.db.IMetricStorage;
import core.models.InstanceMetric;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class Program {


	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws SQLException {
		long start = System.currentTimeMillis();

		SpringService.run();
		IMetricStorage metricStorage = SpringService.getMetricStorage();
		HostService hosts = SpringService.getHosts();
		for (SSHConfiguration host : hosts.getAll()) {
			metricStorage.addInstMetric(host, metricStorage.getTemplateMetric(8));
		}
		List<InstanceMetric> instanceMetricsList = metricStorage.getInstMetrics(1);
		for (InstanceMetric instance : instanceMetricsList) {
			System.out.println(instance.getTitle());
		}


		start = System.currentTimeMillis() - start;
		System.out.println("Время выполнения(мс): "+start);
		System.exit(0);
	}

}
