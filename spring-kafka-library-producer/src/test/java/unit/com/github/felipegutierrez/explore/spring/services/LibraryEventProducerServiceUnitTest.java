package com.github.felipegutierrez.explore.spring.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.felipegutierrez.explore.spring.domain.Book;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.concurrent.ExecutionException;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_V1_TOPIC;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibraryEventProducerServiceUnitTest {

    @Mock
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Spy
    ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    LibraryEventProducerService libraryEventProducerService;

    @Test
    void sendLibraryEvent_failure() {
        // given a book
        Book book = Book.builder()
                .bookId(584)
                .bookAuthor("Felipe")
                .bookName("Why is snowing more in the winter of 2021 in Berlin?")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();

        // when
        SettableListenableFuture settableListenableFuture = new SettableListenableFuture();
        settableListenableFuture.setException(new RuntimeException("simulating an exception when calling Kafka broker"));
        when(kafkaTemplate.sendDefault(isA(Integer.class), isA(String.class))).thenReturn(settableListenableFuture);

        assertThrows(Exception.class, () -> libraryEventProducerService.sendLibraryEvent(libraryEvent).get());
    }

    @Test
    void sendLibraryEventWithProducerRecord_failure() {
        // given a book
        Book book = Book.builder()
                .bookId(584)
                .bookAuthor("Felipe")
                .bookName("Why is snowing more in the winter of 2021 in Berlin?")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();

        // when
        SettableListenableFuture settableListenableFuture = new SettableListenableFuture();
        settableListenableFuture.setException(new RuntimeException("simulating an exception when calling Kafka broker"));
        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(settableListenableFuture);

        assertThrows(Exception.class, () -> libraryEventProducerService.sendLibraryEventWithProducerRecord(libraryEvent).get());
    }

    @Test
    void sendLibraryEvent_success() throws JsonProcessingException, ExecutionException, InterruptedException {
        // given a book
        Book book = Book.builder()
                .bookId(584)
                .bookAuthor("Felipe")
                .bookName("Why is snowing more in the winter of 2021 in Berlin?")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();
        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        ProducerRecord<Integer, String> producerRecord = new ProducerRecord<>(LIBRARY_V1_TOPIC, key, value);
        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition(LIBRARY_V1_TOPIC, 1), 1, 1, 342, System.currentTimeMillis(), 1, 2);
        SendResult<Integer, String> sendResult = new SendResult<Integer, String>(producerRecord, recordMetadata);

        // when
        SettableListenableFuture settableListenableFuture = new SettableListenableFuture();
        settableListenableFuture.set(sendResult);
        // when(kafkaTemplate.sendDefault(isA(Integer.class), isA(String.class))).thenReturn(settableListenableFuture);
        when(kafkaTemplate.sendDefault(any(), isA(String.class))).thenReturn(settableListenableFuture);
        ListenableFuture<SendResult<Integer, String>> listenableFuture = libraryEventProducerService.sendLibraryEvent(libraryEvent);

        // then
        SendResult<Integer, String> sendResultActual = listenableFuture.get();
        assert sendResultActual.getRecordMetadata().partition() == 1;
    }

    @Test
    void sendLibraryEventWithProducerRecord_success() throws JsonProcessingException, ExecutionException, InterruptedException {
        // given a book
        Book book = Book.builder()
                .bookId(584)
                .bookAuthor("Felipe")
                .bookName("Why is snowing more in the winter of 2021 in Berlin?")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();
        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        ProducerRecord<Integer, String> producerRecord = new ProducerRecord<>(LIBRARY_V1_TOPIC, key, value);
        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition(LIBRARY_V1_TOPIC, 1),
                1, 1, 342, System.currentTimeMillis(), 1, 2);
        SendResult<Integer, String> sendResult = new SendResult<Integer, String>(producerRecord, recordMetadata);

        // when
        SettableListenableFuture settableListenableFuture = new SettableListenableFuture();
        settableListenableFuture.set(sendResult);
        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(settableListenableFuture);
        ListenableFuture<SendResult<Integer, String>> listenableFuture = libraryEventProducerService.sendLibraryEventWithProducerRecord(libraryEvent);

        // then
        SendResult<Integer, String> sendResultActual = listenableFuture.get();
        assert sendResultActual.getRecordMetadata().partition() == 1;
    }
}
