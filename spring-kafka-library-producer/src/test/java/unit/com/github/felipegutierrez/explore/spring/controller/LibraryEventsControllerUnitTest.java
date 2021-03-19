package com.github.felipegutierrez.explore.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.felipegutierrez.explore.spring.domain.Book;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import com.github.felipegutierrez.explore.spring.producer.LibraryEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_V1_ENDPOINT;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * We are doing a unit test for the LibraryEventsController and we mock behaviors of the beans inside this class
 * using @MockBean spring annotation for Mockito.
 */
@WebMvcTest(LibraryEventsController.class)
@AutoConfigureMockMvc
public class LibraryEventsControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    LibraryEventProducer libraryEventProducer;

    @Test
        // @Timeout(value = 5000, unit = TimeUnit.MILLISECONDS)
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
        String libraryEventJson = objectMapper.writeValueAsString(libraryEvent);

        /** This is a unit test so we mock the behavior of the sendLibraryEventWithProducerRecord()
         *  method of the LibraryEventProducer. */
        doNothing().when(libraryEventProducer).sendLibraryEventWithProducerRecord(libraryEvent);

        // when
        mockMvc.perform(post(LIBRARY_V1_ENDPOINT)
                .content(libraryEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // then

    }
}
