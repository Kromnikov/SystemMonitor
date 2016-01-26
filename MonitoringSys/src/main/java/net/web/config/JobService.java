//package net.web.config;
//
//import net.core.branches.ScheduledTask;
//import net.core.db.IMetricStorage;
//import net.core.hibernate.services.HostService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//@Controller
//@Service
//@Configuration
//@ComponentScan("net")
//public class JobService implements SchedulingConfigurer {
//
//
////    @Autowired
////    private IMetricStorage metricStorage;
////
////    @Autowired
////    private HostService hosts;
////
////    public JobService() {
////
////    }
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        taskRegistrar.setScheduler(taskExecutor());
//    }
//
//    @Bean(destroyMethod="shutdown")
//    public Executor taskExecutor() {
//        return Executors.newScheduledThreadPool(10);
//    }
//
//    @Bean
//    public ScheduledTask myTask() {
//        return new ScheduledTask();
//    }
////    @Bean
////    public ScheduledTask myTask() {
////        return new ScheduledTask(metricStorage,hosts);
////    }
//}
