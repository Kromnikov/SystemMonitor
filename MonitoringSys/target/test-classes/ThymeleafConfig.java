//package net.web.config;
//
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.thymeleaf.dialect.IDialect;
//import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
//import org.thymeleaf.spring4.SpringTemplateEngine;
//import org.thymeleaf.spring4.view.ThymeleafViewResolver;
//import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Configuration
//@ComponentScan
//@EnableAutoConfiguration
//public class ThymeleafConfig {
//
//    @Bean
//    public ClassLoaderTemplateResolver emailTemplateResolver() {
//        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
//        resolver.setPrefix("templates/");
//        resolver.setSuffix(".html");
//        resolver.setTemplateMode("HTML5");
//        resolver.setCacheable(false);
//        resolver.setCharacterEncoding("UTF-8");
//        resolver.setOrder(1);
//        return resolver;
//    }
////    @Bean
////    ServletContextTemplateResolver templateResolver(){
////        ServletContextTemplateResolver resolver=new ServletContextTemplateResolver();
////        resolver.setSuffix(".html");
////        resolver.setPrefix("templates/");
////        resolver.setTemplateMode("HTML5");
////        return resolver;
////    }
//
//    @Bean
//    public SpringTemplateEngine templateEngine() {
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//
//        Set<IDialect> dialects = new HashSet<IDialect>();
//        dialects.add(springSecurityDialect());
//
//        templateEngine.setAdditionalDialects(dialects);
//        templateEngine.setTemplateResolver(emailTemplateResolver());
//        return templateEngine;
//    }
//
//    @Bean
//    public ThymeleafViewResolver thymeleafViewResolver() {
//        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//        resolver.setTemplateEngine(templateEngine());
//        return resolver;
//    }
//
//    @Bean
//    public SpringSecurityDialect springSecurityDialect() {
//        SpringSecurityDialect dialect = new SpringSecurityDialect();
//        return dialect;
//    }
//}