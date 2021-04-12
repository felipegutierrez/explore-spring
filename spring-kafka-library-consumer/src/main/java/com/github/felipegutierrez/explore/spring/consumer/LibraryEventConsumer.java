package com.github.felipegutierrez.explore.spring.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.felipegutierrez.explore.spring.service.LibraryEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.github.felipegutierrez.explore.spring.util.LibraryConsumerConstants.LIBRARY_V1_TOPIC;

/**
 * If @Component is disable this bean is not going to be recognized as a @KafkaListener to the topic
 */
@Component
@Slf4j
public class LibraryEventConsumer {

    @Autowired
    private LibraryEventService libraryEventService;

    @KafkaListener(topics = {LIBRARY_V1_TOPIC})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        log.info("ConsumerRecord received: {}", consumerRecord);

        libraryEventService.processLibraryEvent(consumerRecord);
    }
}
