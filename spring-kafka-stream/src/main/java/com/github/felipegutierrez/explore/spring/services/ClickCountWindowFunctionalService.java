package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.model.UserClick;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.function.Function;

@Slf4j
@Configuration
@EnableAutoConfiguration
public class ClickCountWindowFunctionalService {

    @Bean
    public Function<KStream<String, UserClick>, KStream<String, String>> clickUsersFunctionalCountWindow() {
        return input -> input
                .peek((key, value) -> log.info("key: {}, CurrentLink: {}, NextLink: {}, Created Time: {}", key, value.getCurrentLink(), value.getNextLink(), Instant.ofEpochMilli(value.getCreatedTime()).atOffset(ZoneOffset.UTC)))
                .groupByKey()
                .windowedBy(SessionWindows.with(Duration.ofMinutes(5)))
                .count()
                .mapValues(value -> String.valueOf(value))
                .toStream()
                .map((windowedId, value) -> new KeyValue<>(windowedId.key(),
                                "UserID: " + windowedId.key() + " Count: " + value
                                // " Window start: " + Instant.ofEpochMilli(windowedId.window().start()).atOffset(ZoneOffset.UTC) +
                                // " Window end: " + Instant.ofEpochMilli(windowedId.window().end()).atOffset(ZoneOffset.UTC) +
                                // " Window#: " + windowedId.window().hashCode()
                        )
                )
                .peek((key, value) -> log.info("key: {}, value: {}", key, value));
    }
}
