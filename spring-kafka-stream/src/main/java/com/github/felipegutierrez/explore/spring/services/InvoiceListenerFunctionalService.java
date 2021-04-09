package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.model.SimpleInvoice;
import com.github.felipegutierrez.explore.spring.utils.CustomSerdes;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.json.KafkaJsonSchemaSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Configuration
@EnableAutoConfiguration
public class InvoiceListenerFunctionalService {

    private final Serde<String> stringSerde;
    private final Serde<SimpleInvoice> simpleInvoiceSerde;

    public InvoiceListenerFunctionalService(@Value("${application.configs.serdes.scope.test}") Boolean scopeTest) {
        if (scopeTest) {
            this.stringSerde = Serdes.String();
            this.simpleInvoiceSerde = new KafkaJsonSchemaSerde<>();

            String SCHEMA_REGISTRY_SCOPE = InvoiceListenerFunctionalService.class.getName();
            String MOCK_SCHEMA_REGISTRY_URL = "mock://" + SCHEMA_REGISTRY_SCOPE;
            Map<String, String> config = Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MOCK_SCHEMA_REGISTRY_URL);
            simpleInvoiceSerde.configure(config, false);
        } else {
            this.stringSerde = CustomSerdes.String();
            this.simpleInvoiceSerde = CustomSerdes.SimpleInvoice();
        }
    }

    @Bean
    public Function<KStream<String, SimpleInvoice>, KStream<String, String>> streamSimpleInvoiceCountWindow() {
        return input -> input
                .peek((k, v) -> log.info("Key = " + k + " Created Time = " + Instant.ofEpochMilli(v.getCreatedTime()).atOffset(ZoneOffset.UTC)))
                .groupByKey(Grouped.with(stringSerde, simpleInvoiceSerde))
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                .count(Materialized.as("InvoiceCounts-1"))
                .toStream()
                .map((k, v) -> new KeyValue<>(k.key(),
                                "StoreID: " + k.key() + " Count: " + v +
                                        " Window start: " + Instant.ofEpochMilli(k.window().start()).atOffset(ZoneOffset.UTC) +
                                        " Window end: " + Instant.ofEpochMilli(k.window().end()).atOffset(ZoneOffset.UTC) +
                                        " Window#: " + k.window().hashCode()
                        )
                );
    }
}
