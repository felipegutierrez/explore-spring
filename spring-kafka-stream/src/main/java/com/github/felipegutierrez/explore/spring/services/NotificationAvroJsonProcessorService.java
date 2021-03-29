package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.PosListenerAvroJsonBinding;
import com.github.felipegutierrez.explore.spring.model.Notification;
import com.github.felipegutierrez.explore.spring.model.PosInvoiceAvro;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
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
        KStream<String, Notification> notificationJsonKStream = input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase(PRIME))
                .mapValues(v -> recordBuilder.getNotificationJson(v));
        notificationJsonKStream.foreach((k, v) -> log.info(String.format("Notification JSON - key: %s, value: %s", k, v)));
        return notificationJsonKStream;
    }
}
