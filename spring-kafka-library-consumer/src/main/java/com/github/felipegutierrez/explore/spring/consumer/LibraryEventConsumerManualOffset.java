package com.github.felipegutierrez.explore.spring.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_V1_TOPIC;

/**
 * If @Component is disable this bean is not going to be recognized as a @KafkaListener to the topic
 */
// @Component
@Slf4j
public class LibraryEventConsumerManualOffset implements AcknowledgingMessageListener<Integer, String> {

    private Integer MESSAGE_BUFFER_SIZE_BEFORE_COMMIT = 5;
    private Integer count = 0;

    @Override
    @KafkaListener(topics = {LIBRARY_V1_TOPIC})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("Manual Offset ConsumerRecord received: {}", consumerRecord);

        count++;

        if (count >= MESSAGE_BUFFER_SIZE_BEFORE_COMMIT) {
            // send the acknowledge to the Message listener telling the success to process the current message
            acknowledgment.acknowledge();
            count = 0;
        }
    }
}
