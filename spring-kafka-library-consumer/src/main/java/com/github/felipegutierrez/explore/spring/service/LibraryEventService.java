package com.github.felipegutierrez.explore.spring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.felipegutierrez.explore.spring.entity.LibraryEvent;
import com.github.felipegutierrez.explore.spring.jpa.LibraryEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Optional;

@Service
@Slf4j
public class LibraryEventService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private LibraryEventRepository libraryEventRepository;

    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    public void processLibraryEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        LibraryEvent libraryEvent = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);
        log.info("LibraryEvent: {}", libraryEvent);

        switch (libraryEvent.getLibraryEventType()) {
            case NEW:
                // save
                save(libraryEvent);
                break;
            case UPDATE:
                // validate
                validate(libraryEvent);
                // update
                save(libraryEvent);
                break;
            default:
                log.info("Invalid LibraryEvent type");
        }
    }

    private void validate(LibraryEvent libraryEvent) {
        // simulating a network failure to test exceptions that allow retries
        if (libraryEvent.getLibraryEventId() != null && libraryEvent.getLibraryEventId() == 000) {
            throw new RecoverableDataAccessException("Temporary Network failure");
        }
        if (libraryEvent.getLibraryEventId() == null) {
            throw new IllegalArgumentException("Library Event ID is missing");
        }
        Optional<LibraryEvent> libraryEventOptional = libraryEventRepository.findById(libraryEvent.getLibraryEventId());
        if (!libraryEventOptional.isPresent()) {
            throw new IllegalArgumentException("Not valid library event");
        }
        log.info("Validation is successful for the library event: {}", libraryEventOptional.get());
    }

    private void save(LibraryEvent libraryEvent) {
        libraryEvent.getBook().setLibraryEvent(libraryEvent);
        libraryEventRepository.save(libraryEvent);
        log.info("Successfully persisted the LibraryEvent: {}", libraryEvent);
    }

    public void handleRecovery(ConsumerRecord<Integer, String> consumerRecord) {
        Integer key = consumerRecord.key();
        String value = consumerRecord.value();
        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key, value);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("message send successfully for the key: {} and value: {} at partition: {}", key, value, result.getRecordMetadata().partition());
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("error sending the message and the exception us {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("error on failure: {}", throwable.getMessage());
        }
    }
}
