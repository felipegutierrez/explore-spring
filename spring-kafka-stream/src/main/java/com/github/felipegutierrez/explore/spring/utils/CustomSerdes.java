package com.github.felipegutierrez.explore.spring.utils;

import com.github.felipegutierrez.explore.spring.model.*;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.confluent.kafka.streams.serdes.json.KafkaJsonSchemaSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig.JSON_VALUE_TYPE;
import static io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializerConfig.FAIL_INVALID_SCHEMA;

@Service
public class CustomSerdes extends Serdes {

    private final static Map<String, String> serdeConfig = Stream.of(
            new AbstractMap.SimpleEntry<>(SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    private final static Map<String, String> serdeConfigNotification = Stream.of(
            new AbstractMap.SimpleEntry<>(SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081")
            , new AbstractMap.SimpleEntry<>(FAIL_INVALID_SCHEMA, "true")
            , new AbstractMap.SimpleEntry<>(JSON_VALUE_TYPE, Notification.class.getName()))
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
        final Serde<Notification> notificationSerde = new KafkaJsonSchemaSerde<>();
        notificationSerde.configure(serdeConfigNotification, false);
        return notificationSerde;
    }

    public static Serde<OrderEnvelop> OrderEnvelop() {
        final Serde<OrderEnvelop> specificJsonSerde = new KafkaJsonSchemaSerde<>();
        specificJsonSerde.configure(serdeConfig, false);
        return specificJsonSerde;
    }

    public static Serde<Employee> EmployeeAvro() {
        final Serde<Employee> employeeSerde = new SpecificAvroSerde<>();
        employeeSerde.configure(serdeConfig, false);
        return employeeSerde;
    }

    public static Serde<DepartmentAggregate> DepartmentAggregateAvro() {
        final Serde<DepartmentAggregate> departmentAggregateSerde = new SpecificAvroSerde<>();
        departmentAggregateSerde.configure(serdeConfig, false);
        return departmentAggregateSerde;
    }

    public static Serde<SimpleInvoice> SimpleInvoice() {
        final Serde<SimpleInvoice> simpleInvoiceSerde = new KafkaJsonSchemaSerde<>();
        simpleInvoiceSerde.configure(serdeConfig, false);
        return simpleInvoiceSerde;
    }
}
