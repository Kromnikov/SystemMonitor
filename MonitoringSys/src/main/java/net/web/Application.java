package net.web;


import net.core.branches.CoreBranch;
import net.web.config.DatabaseConfig;
import net.web.config.JobService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
//@Configuration
@ComponentScan

//scheduler
@EnableCaching
//@EnableAsync
@EnableScheduling

//@ContextConfiguration(locations = {"classpath:/beans.xml"})
public class Application extends SpringBootServletInitializer{

    public static void main(String[] args) throws Exception {
//        SpringApplication.run("classpath:/META-INF/beans.xml", args);
        SpringApplication.run(new Class<?>[]{Application.class, DatabaseConfig.class, JobService.class}, args);
//        CoreBranch.run();
    }

}
