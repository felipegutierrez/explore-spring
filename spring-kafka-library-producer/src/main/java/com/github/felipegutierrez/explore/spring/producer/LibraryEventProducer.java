package com.github.felipegutierrez.explore.spring.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class LibraryEventProducer {

    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * asynchronously
     *
     * @param libraryEvent
     * @throws JsonProcessingException
     */
    public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {

        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

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

    /**
     * synchronous
     *
     * @param libraryEvent
     * @throws JsonProcessingException
     */
    public SendResult<Integer, String> sendLibraryEventBlocking(LibraryEvent libraryEvent) throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {

        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        SendResult<Integer, String> result = null;
        try {
            result = kafkaTemplate.sendDefault(key, value).get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            log.error("InterruptedException | ExecutionException | TimeoutException sending the message and the exception us {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Exception sending the message and the exception us {}", ex.getMessage());
            throw ex;
        }
        return result;
    }
}
