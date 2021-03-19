package com.github.felipegutierrez.explore.spring.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.felipegutierrez.explore.spring.domain.Book;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.SettableListenableFuture;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibraryEventProducerUnitTest {

    @Mock
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Spy
    ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    LibraryEventProducer libraryEventProducer;

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

        assertThrows(Exception.class, () -> libraryEventProducer.sendLibraryEvent(libraryEvent).get());
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

        assertThrows(Exception.class, () -> libraryEventProducer.sendLibraryEventWithProducerRecord(libraryEvent).get());
    }
}
