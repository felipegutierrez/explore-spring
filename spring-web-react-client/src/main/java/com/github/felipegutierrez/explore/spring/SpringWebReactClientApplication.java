package com.github.felipegutierrez.explore.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class SpringWebReactClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebReactClientApplication.class, args);
	}

}
