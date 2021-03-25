package com.github.felipegutierrez.explore.spring.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_V1_TOPIC;

@Component
@Slf4j
public class LibraryEventAvroProducerService {

    @Autowired
    KafkaTemplate<Integer, LibraryEvent> kafkaTemplate;

    /**
     * asynchronously
     *
     * @param libraryEvent
     * @return ListenableFuture<SendResult < Integer, String>> to facilitate unit tests
     * @throws JsonProcessingException
     */
    public ListenableFuture<SendResult<Integer, LibraryEvent>> sendLibraryEventWithProducerRecord(LibraryEvent libraryEvent) throws JsonProcessingException {

        Integer key = libraryEvent.getLibraryEventId();

        ProducerRecord<Integer, LibraryEvent> producerRecord = buildProducerRecord(LIBRARY_V1_TOPIC, key, libraryEvent);

        ListenableFuture<SendResult<Integer, LibraryEvent>> listenableFuture = kafkaTemplate.send(producerRecord);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, LibraryEvent>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, libraryEvent, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, LibraryEvent> result) {
                handleSuccess(key, libraryEvent, result);
            }
        });
        return listenableFuture;
    }

    private ProducerRecord<Integer, LibraryEvent> buildProducerRecord(String topic, Integer key, LibraryEvent value) {

        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));

        return new ProducerRecord<Integer, LibraryEvent>(topic, null, key, value, recordHeaders);
    }

    private void handleSuccess(Integer key, LibraryEvent value, SendResult<Integer, LibraryEvent> result) {
        log.info("message send successfully for the key: {} and value: {} at partition: {}", key, value, result.getRecordMetadata().partition());
    }

    private void handleFailure(Integer key, LibraryEvent value, Throwable ex) {
        log.error("error sending the message and the exception us {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("error on failure: {}", throwable.getMessage());
        }
    }
}
