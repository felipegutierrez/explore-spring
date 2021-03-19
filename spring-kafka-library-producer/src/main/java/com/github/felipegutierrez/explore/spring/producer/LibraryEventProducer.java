package com.github.felipegutierrez.explore.spring.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_V1_TOPIC;

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
     * @return ListenableFuture<SendResult<Integer, String>> to facilitate unit tests
     * @throws JsonProcessingException
     */
    public ListenableFuture<SendResult<Integer, String>> sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {

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
        return listenableFuture;
    }

    /**
     * asynchronously
     *
     * @param libraryEvent
     * @return ListenableFuture<SendResult<Integer, String>> to facilitate unit tests
     * @throws JsonProcessingException
     */
    public ListenableFuture<SendResult<Integer, String>> sendLibraryEventWithProducerRecord(LibraryEvent libraryEvent) throws JsonProcessingException {

        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        ProducerRecord<Integer, String> producerRecord = buildProducerRecord(LIBRARY_V1_TOPIC, key, value);

        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(producerRecord);
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
        return listenableFuture;
    }

    private ProducerRecord<Integer, String> buildProducerRecord(String topic, Integer key, String value) {

        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));

        return new ProducerRecord<Integer, String>(topic, null, key, value, recordHeaders);
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
