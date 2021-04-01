package com.github.felipegutierrez.explore.spring.utils;

import com.github.felipegutierrez.explore.spring.model.HadoopRecordAvro;
import com.github.felipegutierrez.explore.spring.model.Notification;
import com.github.felipegutierrez.explore.spring.model.NotificationAvro;
import com.github.felipegutierrez.explore.spring.model.OrderEnvelop;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.confluent.kafka.streams.serdes.json.KafkaJsonSchemaSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig.*;

@Service
public class CustomSerdes extends Serdes {

    private final static Map<String, String> serdeConfig = Stream.of(
            new AbstractMap.SimpleEntry<>(SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081")
            , new AbstractMap.SimpleEntry<>(JSON_VALUE_TYPE, "com.fasterxml.jackson.databind.JsonNode")
            // , new AbstractMap.SimpleEntry<>(JSON_VALUE_TYPE, "com.github.felipegutierrez.explore.spring.model.Notification")
            // , new AbstractMap.SimpleEntry<>(JSON_VALUE_TYPE, Notification.class.getName())
            // , new AbstractMap.SimpleEntry<>(JSON_VALUE_TYPE, "com.fasterxml.jackson.databind.JavaType")
            // , new AbstractMap.SimpleEntry<>(TYPE_PROPERTY, TYPE_PROPERTY_DEFAULT)
            // , new AbstractMap.SimpleEntry<>("spring.json.add.type.headers", "false")
            // , new AbstractMap.SimpleEntry<>("json.value.type", "org.springframework.kafka.support.serializer.JsonSerializer")
    )
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

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

    public static Serde<Notification> Notification() {
        final Serde<Notification> notificationSerde = new KafkaJsonSchemaSerde<Notification>();
        notificationSerde.configure(serdeConfig, false);
        return notificationSerde;
    }

    public static Serde<Notification> NotificationJsonSerde() {
        final Serde<Notification> notificationSerde = new JsonSerde<Notification>();
        notificationSerde.configure(serdeConfig, false);
        return notificationSerde;
    }

    public static Serde<OrderEnvelop> OrderEnvelop() {
        final Serde<OrderEnvelop> specificJsonSerde = new KafkaJsonSchemaSerde<>();
        specificJsonSerde.configure(serdeConfig, false);
        return specificJsonSerde;
    }
}
