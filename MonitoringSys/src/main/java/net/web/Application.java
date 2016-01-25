package net.web;


import net.core.branches.CoreBranch;
import net.web.config.DatabaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
//@EnableAutoConfiguration
@ComponentScan
public class Application extends SpringBootServletInitializer{

    public static void main(String[] args) throws Exception {
        //JSPController
        //IndexController
        //ApplicationContext context = SpringApplication.run(Application.class, args);
//        SpringApplication.run(IndexController.class, args);
        SpringApplication.run(new Class<?>[] {Application.class, DatabaseConfig.class}, args);
        CoreBranch.run();

        //CoreBranch.run();

//        SpringApplication.run("classpath:/META-INF/beans.xml", args);
    }

}
