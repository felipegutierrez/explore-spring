package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.PosListenerAvroExactlyOnceBinding;
import com.github.felipegutierrez.explore.spring.model.HadoopRecordAvro;
import com.github.felipegutierrez.explore.spring.model.NotificationAvro;
import com.github.felipegutierrez.explore.spring.model.PosInvoiceAvro;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import static com.github.felipegutierrez.explore.spring.utils.PosInvoiceConstants.PRIME;

@Service
@Slf4j
@EnableBinding(PosListenerAvroExactlyOnceBinding.class)
public class PosListenerAvroExactlyOnceService {

    @Autowired
    RecordBuilder recordBuilder;

    /**
     * Consume KStream from the channel "pos-input-avro-exactly-once-channel" using exactly-once semantic and output to the topics
     * "hadoop-sink-avro-ex-topic" and "loyalty-avro-ex-topic".
     * @param input
     */
    @StreamListener("pos-input-avro-exactly-once-channel")
    public void process(KStream<String, PosInvoiceAvro> input) {
        KStream<String, HadoopRecordAvro> hadoopRecordKStream = input
                .mapValues(v -> recordBuilder.getMaskedInvoiceAvro(v))
                .flatMapValues(v -> recordBuilder.getHadoopRecordsAvro(v));

        KStream<String, NotificationAvro> notificationKStream = input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase(PRIME))
                .mapValues(v -> recordBuilder.getNotificationAvro(v));

        hadoopRecordKStream.foreach((k, v) -> log.info(String.format("Hadoop Record AVRO:- Key: %s, Value: %s", k, v)));
        notificationKStream.foreach((k, v) -> log.info(String.format("Notification  AVRO:- Key: %s, Value: %s", k, v)));

        hadoopRecordKStream.to("hadoop-sink-avro-ex-topic");
        notificationKStream.to("loyalty-avro-ex-topic");
    }
}
