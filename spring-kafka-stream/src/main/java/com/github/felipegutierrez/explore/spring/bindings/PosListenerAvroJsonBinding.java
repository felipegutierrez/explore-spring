package com.github.felipegutierrez.explore.spring.bindings;

import com.github.felipegutierrez.explore.spring.model.HadoopRecord;
import com.github.felipegutierrez.explore.spring.model.Notification;
import com.github.felipegutierrez.explore.spring.model.PosInvoiceAvro;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

/**
 * Binding interface to map the Spring-stream channel with the record type
 */
public interface PosListenerAvroJsonBinding {

    @Input("notification-input-avro-channel")
    KStream<String, PosInvoiceAvro> notificationAvroInputStream();

    @Output("notification-output-json-channel")
    KStream<String, Notification> notificationJsonOutputStream();

    @Input("hadoop-input-avro-channel")
    KStream<String, PosInvoiceAvro> hadoopAvroInputStream();

    @Output("hadoop-output-json-channel")
    KStream<String, HadoopRecord> hadoopJsonOutputStream();
}
