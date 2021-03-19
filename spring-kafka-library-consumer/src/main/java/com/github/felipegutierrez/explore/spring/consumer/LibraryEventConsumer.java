package com.github.felipegutierrez.explore.spring.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_V1_TOPIC;

@Component
@Slf4j
public class LibraryEventConsumer {

    @KafkaListener(topics = {LIBRARY_V1_TOPIC})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) {
        log.info("ConsumerRecord received: {}", consumerRecord);
    }
}
