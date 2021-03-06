package com.github.felipegutierrez.explore.spring.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Slf4j
@Configuration
@EnableAutoConfiguration
public class KafkaListenerFunctionalService {

    @Bean
    public Function<KStream<String, String>, KStream<String, String>> transformToUpperCase() {
        return input -> input
                .peek((k, v) -> log.info("Functional received Input: {}", v))
                .mapValues(i -> i.toUpperCase());
    }
}
