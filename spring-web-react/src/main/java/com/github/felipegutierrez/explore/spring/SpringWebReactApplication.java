package com.github.felipegutierrez.explore.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class SpringWebReactApplication {

    public static void main(String[] args) {
        disclaimer();
        SpringApplication.run(SpringWebReactApplication.class, args);
    }

    private static void disclaimer() {
        System.out.println("start MongoDB first: 'sudo docker-compose up'");
    }
}
