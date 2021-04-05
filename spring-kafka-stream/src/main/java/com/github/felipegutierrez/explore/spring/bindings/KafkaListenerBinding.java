package com.github.felipegutierrez.explore.spring.bindings;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface KafkaListenerBinding {
    @Input("process-in-0")
    KStream<String, String> inputStream();

    @Output("process-out-0")
    KStream<String, String> outStream();
}
