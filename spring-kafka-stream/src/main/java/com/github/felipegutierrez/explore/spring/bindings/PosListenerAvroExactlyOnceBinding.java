package com.github.felipegutierrez.explore.spring.bindings;

import com.github.felipegutierrez.explore.spring.model.PosInvoiceAvro;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

/**
 * A Binding listener to Avro input messages using exactly-once semantics and let the output channel be decided on the service.
 */
public interface PosListenerAvroExactlyOnceBinding {

    @Input("pos-input-avro-exactly-once-channel")
    KStream<String, PosInvoiceAvro> notificationAvroInputExactlyOnceStream();
}
