package com.github.felipegutierrez.explore.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@Profile("dev")
@Slf4j
public class AutoCreateConfig {

    private final Integer numberOfPartitions = 3;

    @Bean
    ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<Object, Object> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory);

        /** Use MANUEL together with [[LibraryEventConsumerManualOffset]] to change the behavior of committing messages. */
        // factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        /** use concurrency set to the number of partitions in case the application is not running on top of Kubernetes */
        factory.setConcurrency(numberOfPartitions);

        factory.setErrorHandler(((thrownException, data) -> {
            log.info("Exception in consumer config is {} and the record is: {}", thrownException.getMessage(), data);
        }));

        factory.setRetryTemplate(retryTemplate());
        return factory;
    }

    private RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3);
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(IllegalArgumentException.class, false);
        retryableExceptions.put(RecoverableDataAccessException.class, true);
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3, retryableExceptions, true);
        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(1_000); // in milliseconds
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        return retryTemplate;
    }
}
