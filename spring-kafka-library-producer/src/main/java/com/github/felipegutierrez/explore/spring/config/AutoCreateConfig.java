package com.github.felipegutierrez.explore.spring.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_V1_TOPIC;

@Configuration
@Profile("dev")
public class AutoCreateConfig {

    /**
     * The topic "library-events" will be create only for the @Profile("dev") profile.
     * Check on the Kafka broker using:
     * "./bin/kafka-topics.sh --zookeeper localhost:2181 --list"
     *
     * @return
     */
    @Bean
    public NewTopic createLibraryEventsTopic() {
        return TopicBuilder.name(LIBRARY_V1_TOPIC)
                .partitions(3)
                .replicas(3)
                .build();
    }
}
