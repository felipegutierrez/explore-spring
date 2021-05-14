package com.github.felipegutierrez.explore.spring;

import com.github.felipegutierrez.explore.spring.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Arrays;

@SpringBootApplication
public class SpringWebApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringWebApplication.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext =
                SpringApplication.run(SpringWebApplication.class, args);
        LOGGER.info("use:");
        LOGGER.info("http://localhost:8080/");
        LOGGER.info("http://localhost:8080/actuator");
        LOGGER.info("http://localhost:8080/books?ids=1,3,4");
        LOGGER.info("http://localhost:8080/book?id=1");
        LOGGER.info("http://localhost:8080/book/searchByName?name=of");
        LOGGER.info("http://localhost:8080/book/searchByAuthor?author=a");

        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(name ->
                LOGGER.info(name)
        );
    }

    // @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // bootstrap the dispatcher servlet
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

        context.register(ApplicationConfig.class);

        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet("spring-mvc", new DispatcherServlet(context));
        // using the eager initialization
        servletRegistration.setLoadOnStartup(1);
        servletRegistration.addMapping("/");
    }
}
