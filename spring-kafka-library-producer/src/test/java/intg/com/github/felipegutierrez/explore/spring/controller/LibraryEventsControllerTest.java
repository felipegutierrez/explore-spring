package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.domain.Book;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_V1_ENDPOINT;
import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_V1_TOPIC;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {LIBRARY_V1_TOPIC}, partitions = 3)
@TestPropertySource(properties = {
        "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"
})
public class LibraryEventsControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void postLibraryEventTest() {
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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent, httpHeaders);

        // when a POST is sent to the @RestController endpoint we should receive a ResponseEntity
        ResponseEntity<LibraryEvent> responseEntity = testRestTemplate
                .exchange(LIBRARY_V1_ENDPOINT, HttpMethod.POST, request, LibraryEvent.class);

        // then assert the response status
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
}
