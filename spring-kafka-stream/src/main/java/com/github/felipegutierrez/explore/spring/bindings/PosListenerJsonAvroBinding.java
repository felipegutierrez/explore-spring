package com.github.felipegutierrez.explore.spring.bindings;

import com.github.felipegutierrez.explore.spring.model.HadoopRecordAvro;
import com.github.felipegutierrez.explore.spring.model.NotificationAvro;
import com.github.felipegutierrez.explore.spring.model.PosInvoice;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

/**
 * Binding interface to map the Spring-stream channel with the record type
 */
public interface PosListenerJsonAvroBinding {

    @Input("notification-input-channel")
    KStream<String, PosInvoice> notificationInputStream();

    @Output("notification-output-avro-channel")
    KStream<String, NotificationAvro> notificationAvroOutputStream();

    @Input("hadoop-input-channel")
    KStream<String, PosInvoice> hadoopInputStream();

    @Output("hadoop-output-avro-channel")
    KStream<String, HadoopRecordAvro> hadoopAvroOutputStream();
}
