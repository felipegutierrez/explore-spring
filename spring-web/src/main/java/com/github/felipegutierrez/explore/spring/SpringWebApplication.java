package com.github.felipegutierrez.explore.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class SpringWebApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringWebApplication.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext =
                SpringApplication.run(SpringWebApplication.class, args);
        LOGGER.info("use:");
        LOGGER.info("http://localhost:8080/books");

        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(name ->
                LOGGER.info(name)
        );
    }
}
