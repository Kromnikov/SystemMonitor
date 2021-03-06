package net.web;


import net.web.config.DatabaseConfig;
import net.web.config.ThymeleafAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication

//scheduler
@EnableCaching
@EnableScheduling

@ComponentScan("net")
public class Application extends SpringBootServletInitializer{

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Class<?>[]{Application.class, DatabaseConfig.class,ThymeleafAutoConfiguration.class}, args);
    }

}
