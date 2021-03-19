package com.github.felipegutierrez.explore.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringKafkaLibraryConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaLibraryConsumerApplication.class, args);

		System.out.println("Start the Kafka broker");
		System.out.println("./bin/zookeeper-server-start.sh config/zookeeper.properties");
		System.out.println("./bin/kafka-server-start.sh config/server.properties");
		System.out.println("./bin/kafka-server-start.sh config/server-01.properties");
		System.out.println("./bin/kafka-server-start.sh config/server-02.properties");
		System.out.println();
		System.out.println("alunching application from command line");
		System.out.println("java -jar -Dserver.port=8081 build/libs/spring-kafka-library-consumer-0.0.1.jar");
		System.out.println("java -jar -Dserver.port=8082 build/libs/spring-kafka-library-consumer-0.0.1.jar");
		System.out.println("java -jar -Dserver.port=8083 build/libs/spring-kafka-library-consumer-0.0.1.jar");
		System.out.println();
		System.out.println("try out commands:");
		System.out.println("http POST http://localhost:8080/v1/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-00.json");
		System.out.println("http PUT  http://localhost:8080/v1/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-01.json");
		System.out.println("http PUT  http://localhost:8080/v1/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-02.json");
	}

}
