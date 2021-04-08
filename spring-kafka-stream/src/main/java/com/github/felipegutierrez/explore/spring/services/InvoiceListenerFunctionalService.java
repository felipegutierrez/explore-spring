package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.model.SimpleInvoice;
import com.github.felipegutierrez.explore.spring.utils.CustomSerdes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
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
public class InvoiceListenerFunctionalService {

    @Bean
    public Function<KStream<String, SimpleInvoice>, KStream<String, String>> streamSimpleInvoiceCountWindow() {
        return input -> input
                .peek((k, v) -> log.info("Key = " + k + " Created Time = " + Instant.ofEpochMilli(v.getCreatedTime()).atOffset(ZoneOffset.UTC)))
                .groupByKey(Grouped.with(CustomSerdes.String(), CustomSerdes.SimpleInvoice()))
                .windowedBy(TimeWindows.of(Duration.ofSeconds(30)))
                .count()
                .toStream()
                .map((k, v) -> new KeyValue<>(k.key(),
                                "StoreID: " + k.key() +
                                        " Window start: " + Instant.ofEpochMilli(k.window().start()).atOffset(ZoneOffset.UTC) +
                                        " Window end: " + Instant.ofEpochMilli(k.window().end()).atOffset(ZoneOffset.UTC) +
                                        " Count: " + v + " Window#: " + k.window().hashCode()
                        )
                );
    }
}
