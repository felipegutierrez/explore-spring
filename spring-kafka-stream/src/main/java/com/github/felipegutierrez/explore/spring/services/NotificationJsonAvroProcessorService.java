package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.PosListenerJsonAvroBinding;
import com.github.felipegutierrez.explore.spring.model.NotificationAvro;
import com.github.felipegutierrez.explore.spring.model.PosInvoice;
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
@EnableBinding(PosListenerJsonAvroBinding.class)
public class NotificationJsonAvroProcessorService {

    @Autowired
    RecordJsonToAvroBuilder recordJsonToAvroBuilder;

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
        KStream<String, NotificationAvro> notificationAvroKStream = input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase(PRIME))
                .mapValues(v -> recordJsonToAvroBuilder.getNotificationAvro(v));
        notificationAvroKStream.foreach((k, v) -> log.info(String.format("Notification avro - key: %s, value: %s", k, v)));
        return notificationAvroKStream;
    }
}
