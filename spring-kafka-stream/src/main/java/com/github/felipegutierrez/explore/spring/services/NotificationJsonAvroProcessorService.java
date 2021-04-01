package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.PosListenerJsonAvroBinding;
import com.github.felipegutierrez.explore.spring.model.NotificationAvro;
import com.github.felipegutierrez.explore.spring.model.PosInvoice;
import com.github.felipegutierrez.explore.spring.utils.CustomSerdes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Serialized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import static com.github.felipegutierrez.explore.spring.utils.PosInvoiceConstants.PRIME;

@Service
@Slf4j
@EnableBinding(PosListenerJsonAvroBinding.class)
public class NotificationJsonAvroProcessorService {

    @Autowired
    RecordBuilder recordBuilder;

    /**
     * receives a stream of PosInvoice in Json format from the "notification-input-channel" channel
     * and output a stream of NotificationAvro in avro format on the "notification-output-avro-channel" channel.
     *
     * @param input
     * @return
     */
    @StreamListener("notification-input-channel")
    @SendTo("notification-output-avro-channel")
    public KStream<String, NotificationAvro> process(KStream<String, PosInvoice> input) {
        log.info("received PosInvoice JSON: {}", input);

        /* without reduce transformation
        KStream<String, NotificationAvro> notificationAvroKStream = input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase(PRIME))
                .mapValues(v -> recordBuilder.getNotificationAvro(v));
        notificationAvroKStream.foreach((k, v) -> log.info(String.format("Notification avro - key: %s, value: %s", k, v)));
        return notificationAvroKStream;
        */

        /* with reduce transformation and serialization with KTable */
        KStream<String, NotificationAvro> notificationAvroKStream = input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase(PRIME))
                .map((k, v) -> new KeyValue<>(v.getCustomerCardNo(), recordBuilder.getNotificationAvro(v)))
                .groupByKey(Serialized.with(CustomSerdes.String(), CustomSerdes.NotificationAvro()))
                .reduce((aggValue, newValue) -> {
                    newValue.setTotalLoyaltyPoints(newValue.getEarnedLoyaltyPoints() + aggValue.getTotalLoyaltyPoints());
                    return newValue;
                })
                .toStream();
        notificationAvroKStream.foreach((k, v) -> log.info(String.format("Notification avro agg - key: %s, value: %s", k, v)));
        return notificationAvroKStream;
    }
}
