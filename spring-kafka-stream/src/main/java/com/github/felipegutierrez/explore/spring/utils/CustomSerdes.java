package com.github.felipegutierrez.explore.spring.utils;

import com.github.felipegutierrez.explore.spring.model.HadoopRecordAvro;
import com.github.felipegutierrez.explore.spring.model.NotificationAvro;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomSerdes extends Serdes {

    private static final String schema_registry_url = "http://localhost:8081";

    private final static Map<String, String> serdeConfig = Collections
            .singletonMap("schema.registry.url", schema_registry_url);

    public static Serde<HadoopRecordAvro> HadoopRecordAvro() {
        final Serde<HadoopRecordAvro> hadoopRecordAvroSerde = new SpecificAvroSerde<>();
        hadoopRecordAvroSerde.configure(serdeConfig, false);
        return hadoopRecordAvroSerde;
    }

    public static Serde<NotificationAvro> NotificationAvro() {
        final Serde<NotificationAvro> notificationAvroSerde = new SpecificAvroSerde<>();
        notificationAvroSerde.configure(serdeConfig, false);
        return notificationAvroSerde;
    }
}
