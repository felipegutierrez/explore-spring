package com.github.felipegutierrez.explore.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.felipegutierrez.explore.spring.domain.Book;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import com.github.felipegutierrez.explore.spring.producer.LibraryEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_REACT_V1_ENDPOINT;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

// @SpringBootTest
// @ExtendWith(SpringExtension.class)
// @DirtiesContext
//@ActiveProfiles("test")
// @AutoConfigureWebTestClient
// @WebMvcTest(LibraryEventsReactController.class)
// @AutoConfigureMockMvc
@WebFluxTest(LibraryEventsReactController.class)
public class LibraryEventsReactControllerUnitTest {

    // @Autowired
    // MockMvc mockMvc;

    @Autowired
    WebTestClient webTestClient;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    LibraryEventProducer libraryEventProducer;

    @Test
    void postLibraryEventTest() throws Exception {
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
        // String libraryEventJson = objectMapper.writeValueAsString(libraryEvent);

        /** This is a unit test so we mock the behavior of the sendLibraryEventWithProducerRecord()
         *  method of the LibraryEventProducer. */
        when(libraryEventProducer.sendLibraryEventWithProducerRecord(isA(LibraryEvent.class))).thenReturn(null);

        webTestClient.post().uri(LIBRARY_REACT_V1_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(libraryEvent), LibraryEvent.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(LibraryEvent.class)
        // .expectBody()
        // .jsonPath("$.libraryEventType", "NEW")
        ;
        // when
        /*
        mockMvc.perform(post(LIBRARY_REACT_V1_ENDPOINT)
                .content(libraryEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        */
    }

    @Test
    public void putLibraryEvent() throws JsonProcessingException {
        // given a book
        Book book = Book.builder()
                .bookId(584)
                .bookAuthor("Felipe")
                .bookName("Why is snowing more in the winter of 2021 in Berlin?")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(123)
                .book(book)
                .build();
        // String libraryEventJson = objectMapper.writeValueAsString(libraryEvent);

        /** This is a unit test so we mock the behavior of the sendLibraryEventWithProducerRecord()
         *  method of the LibraryEventProducer. */
        when(libraryEventProducer.sendLibraryEventWithProducerRecord(isA(LibraryEvent.class))).thenReturn(null);

        webTestClient.put().uri(LIBRARY_REACT_V1_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(libraryEvent), LibraryEvent.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void putLibraryEvent_BadRequest() throws JsonProcessingException {
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
        // String libraryEventJson = objectMapper.writeValueAsString(libraryEvent);

        /** This is a unit test so we mock the behavior of the sendLibraryEventWithProducerRecord()
         *  method of the LibraryEventProducer. */
        when(libraryEventProducer.sendLibraryEventWithProducerRecord(isA(LibraryEvent.class))).thenReturn(null);

        webTestClient.put().uri(LIBRARY_REACT_V1_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(libraryEvent), LibraryEvent.class)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
