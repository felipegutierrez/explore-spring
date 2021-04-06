package com.github.felipegutierrez.explore.spring.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.function.Function;

@Slf4j
@Configuration
@EnableAutoConfiguration
public class WordListenerFunctionalService {

    @Bean
    public Function<KStream<String, String>, KStream<String, String>> streamFunctionalWordCount() {
        return input -> input
                .peek((k, v) -> log.info("Functional word count received Input: {}", v))
                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split(" ")))
                .groupBy((key, value) -> value)
                .count()
                .mapValues(value -> String.valueOf(value))
                .toStream()
                .peek((k, v) -> log.info("Word: [{}] Count: [{}]", k, v));
    }
}
