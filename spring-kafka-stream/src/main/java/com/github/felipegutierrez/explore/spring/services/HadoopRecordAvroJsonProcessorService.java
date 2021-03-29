package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.PosListenerAvroJsonBinding;
import com.github.felipegutierrez.explore.spring.model.HadoopRecord;
import com.github.felipegutierrez.explore.spring.model.PosInvoiceAvro;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableBinding(PosListenerAvroJsonBinding.class)
public class HadoopRecordAvroJsonProcessorService {

    @Autowired
    RecordBuilder recordBuilder;

    /**
     * receives a stream of PosInvoice in Json format from the "hadoop-input-channel" channel
     * and output a stream of NotificationAvro in avro format on the "hadoop-output-avro-channel" channel.
     */
    @StreamListener("hadoop-input-avro-channel")
    @SendTo("hadoop-output-json-channel")
    public KStream<String, HadoopRecord> process(KStream<String, PosInvoiceAvro> input) {
        KStream<String, HadoopRecord> hadoopRecordJsonKStream = input
                .mapValues(v -> recordBuilder.getMaskedInvoiceAvro(v))
                .flatMapValues(v -> recordBuilder.getHadoopRecordsJson(v));

        hadoopRecordJsonKStream.foreach((k, v) -> log.info(String.format("Hadoop record JSON - key: %s, value: %s", k, v)));
        return hadoopRecordJsonKStream;
    }
}
