package core;

import core.configurations.SSHConfiguration;
import core.hibernate.services.HostService;
import org.apache.log4j.Logger;

public class Program {

    private static final Logger logger = Logger.getLogger(Program.class);

    public static void main(String[] args) {
//		ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/beans.xml");
//		HostService service = (HostService) context.getBean("HostService");
////		service.save(new SSHConfiguration("192.168.56.101", 22, "kromnikov", "12345"));
//		logger.info("Список всех элементов библиотеки мультимедиа:");
//		for (SSHConfiguration sshConfiguration : service.getAll()) {
//			logger.info(sshConfiguration.getHost());
////            System.out.println(sshConfiguration.getHost());
//        }


		SpringService.run();
		for (SSHConfiguration sshConfiguration : ((HostService) SpringService.getContext().getBean("HostService")).getAll() ) {
			System.out.println(sshConfiguration);
		}
	}

}
