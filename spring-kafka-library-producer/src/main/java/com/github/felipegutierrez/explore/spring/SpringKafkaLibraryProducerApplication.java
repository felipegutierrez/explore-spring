package com.github.felipegutierrez.explore.spring;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringKafkaLibraryProducerApplication {

	@Value("${topic.name}")
	private String topicName;

	@Value("${topic.partitions-num}")
	private Integer partitions;

	@Value("${topic.replication-factor}")
	private short replicationFactor;

	@Bean
	NewTopic libraryEventTopic() {
		return new NewTopic(topicName, partitions, replicationFactor);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaLibraryProducerApplication.class, args);

		System.out.println("Start the Kafka broker");
		System.out.println("./bin/zookeeper-server-start.sh config/zookeeper.properties");
		System.out.println("./bin/kafka-server-start.sh config/server.properties");
		System.out.println("./bin/kafka-server-start.sh config/server-01.properties");
		System.out.println("./bin/kafka-server-start.sh config/server-02.properties");
		System.out.println();
		System.out.println("try out commands:");
		System.out.println("http POST http://localhost:8080/v1/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-00.json");
		System.out.println("http PUT  http://localhost:8080/v1/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-01.json");
		System.out.println("http PUT  http://localhost:8080/v1/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-02.json");
		// System.out.println("http POST http://localhost:8080/v1/libraryevent/avro < spring-kafka-library-producer/src/main/resources/static/libraryEvent-00.json");
	}

}
