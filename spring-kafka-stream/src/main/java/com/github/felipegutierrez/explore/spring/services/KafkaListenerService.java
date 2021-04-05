package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.KafkaListenerBinding;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableBinding(KafkaListenerBinding.class)
public class KafkaListenerService {

    @StreamListener("process-in-0")
    @SendTo("process-out-0")
    public KStream<String, String> transformToUpperCase(KStream<String, String> input) {
        return input
                .peek((k, v) -> log.info("Received Input: {}", v))
                .mapValues(v -> v.toUpperCase())
                .peek((k, v) -> log.info("transformed to Output: {}", v));
    }
}
