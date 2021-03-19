package com.github.felipegutierrez.explore.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringKafkaLibraryProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaLibraryProducerApplication.class, args);

		System.out.println("try out commands:");
		System.out.println("http POST http://localhost:8080/v1/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-00.json");
		System.out.println("http PUT  http://localhost:8080/v1/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-01.json");
		System.out.println("http PUT  http://localhost:8080/v1/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-02.json");
	}

}
