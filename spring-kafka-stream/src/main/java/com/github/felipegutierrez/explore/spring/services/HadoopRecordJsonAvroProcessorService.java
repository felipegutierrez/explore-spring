package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.PosListenerJsonAvroBinding;
import com.github.felipegutierrez.explore.spring.model.HadoopRecordAvro;
import com.github.felipegutierrez.explore.spring.model.PosInvoice;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableBinding(PosListenerJsonAvroBinding.class)
public class HadoopRecordJsonAvroProcessorService {

    @Autowired
    RecordBuilder recordBuilder;

    /**
     * receives a stream of PosInvoice in Json format from the "hadoop-input-channel" channel
     * and output a stream of NotificationAvro in avro format on the "hadoop-output-avro-channel" channel.
     */
    @StreamListener("hadoop-input-channel")
    @SendTo("hadoop-output-avro-channel")
    public KStream<String, HadoopRecordAvro> process(KStream<String, PosInvoice> input) {
        KStream<String, HadoopRecordAvro> hadoopRecordAvroKStream = input
                .mapValues(v -> recordBuilder.getMaskedInvoiceJson(v))
                .flatMapValues(v -> recordBuilder.getHadoopRecordsAvro(v));

        hadoopRecordAvroKStream.foreach((k, v) -> log.info(String.format("Hadoop record avro - key: %s, value: %s", k, v)));
        return hadoopRecordAvroKStream;
    }
}
