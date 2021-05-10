package com.github.felipegutierrez.explore.spring.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Slf4j
// @Configuration
// @EnableAutoConfiguration
public class KafkaAggFunctionalService {

    // @Bean
    public Function<KTable<String, String>, KStream<String, String>> aggregate() {
        return table -> table
                .toStream()
                .groupBy((key, value) -> key, Grouped.with(Serdes.String(), Serdes.String()))
                .aggregate(() -> "", (key, value, aggregate) ->
                                value,
                        Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as("test-events-snapshots").withKeySerde(Serdes.String()).withValueSerde(Serdes.String())
                )
                .toStream()
                .peek((k, v) -> log.info("Received key={}, value={}", k, v));
    }
}
