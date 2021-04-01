package com.github.felipegutierrez.explore.spring.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.felipegutierrez.explore.spring.bindings.PosListenerAvroJsonBinding;
import com.github.felipegutierrez.explore.spring.model.Notification;
import com.github.felipegutierrez.explore.spring.model.PosInvoiceAvro;
import com.github.felipegutierrez.explore.spring.utils.CustomSerdes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import static com.github.felipegutierrez.explore.spring.utils.PosInvoiceConstants.PRIME;

@Service
@Slf4j
@EnableBinding(PosListenerAvroJsonBinding.class)
public class NotificationAvroJsonProcessorService {

    final Serializer<JsonNode> jsonNodeSerializer = new JsonSerializer();
    final Deserializer<JsonNode> jsonNodeDeserializer = new JsonDeserializer();
    final Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonNodeSerializer, jsonNodeDeserializer);

    @Autowired
    RecordBuilder recordBuilder;

    /**
     * receives a stream of PosInvoice in Json format from the "notification-input-avro-channel" channel
     * and output a stream of NotificationAvro in avro format on the "notification-output-json-channel" channel.
     *
     * @param input
     * @return
     */
    @StreamListener("notification-input-avro-channel")
    @SendTo("notification-output-json-channel")
    public KStream<String, Notification> process(KStream<String, PosInvoiceAvro> input) {
        log.info("received PosInvoiceAvro stream: {}", input);

        /* without reduce transformation
        KStream<String, Notification> notificationJsonKStream = input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase(PRIME))
                .mapValues(v -> recordBuilder.getNotificationJson(v));
        notificationJsonKStream.foreach((k, v) -> log.info(String.format("Notification JSON - key: %s, value: %s", k, v)));
        return notificationJsonKStream;
         */

        /* with reduce transformation and serialization with KTable */
        KStream<String, Notification> notificationJsonKStream = input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase(PRIME))
                .map((k, v) -> new KeyValue<>(v.getCustomerCardNo(), recordBuilder.getNotificationJson(v)))
                .groupByKey(Grouped.with(CustomSerdes.String(), CustomSerdes.Notification()))
                .reduce((aggValue, newValue) -> {
                            newValue.setTotalLoyaltyPoints(newValue.getEarnedLoyaltyPoints() + aggValue.getTotalLoyaltyPoints());
                            return newValue;
                        },
                        Named.as("notification-reducer"),
                        Materialized.with(CustomSerdes.String(), CustomSerdes.Notification()))
                .toStream();
        notificationJsonKStream.foreach((k, v) -> log.info(String.format("Notification JSON agg - key: %s, value: %s", k, v)));
        return notificationJsonKStream;
    }
}
