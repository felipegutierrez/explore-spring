package com.github.felipegutierrez.explore.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.felipegutierrez.explore.spring.controllers.LibraryEventsController;
import com.github.felipegutierrez.explore.spring.domain.Book;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import com.github.felipegutierrez.explore.spring.services.LibraryEventProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.felipegutierrez.explore.spring.util.LibraryProducerConstants.LIBRARY_V1_ENDPOINT;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * We are doing a unit test for the LibraryEventsController and we mock behaviors of the beans inside this class
 * using @MockBean spring annotation for Mockito.
 */
@WebMvcTest(LibraryEventsController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    LibraryEventProducerService libraryEventProducerService;

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
        String libraryEventJson = objectMapper.writeValueAsString(libraryEvent);

        /** This is a unit test so we mock the behavior of the sendLibraryEventWithProducerRecord()
         *  method of the LibraryEventProducer. */
        when(libraryEventProducerService.sendLibraryEventWithProducerRecord(isA(LibraryEvent.class))).thenReturn(null);

        // when
        mockMvc.perform(post(LIBRARY_V1_ENDPOINT)
                .content(libraryEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void postLibraryEventTestWithNullBook() throws Exception {
        // given a library event with no book
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(null)
                .build();
        String libraryEventJson = objectMapper.writeValueAsString(libraryEvent);

        /** This is a unit test so we mock the behavior of the sendLibraryEventWithProducerRecord()
         *  method of the LibraryEventProducer. */
        when(libraryEventProducerService.sendLibraryEventWithProducerRecord(isA(LibraryEvent.class))).thenReturn(null);

        // when
        String expectedErrorMessage = "book - must not be null";
        mockMvc.perform(post(LIBRARY_V1_ENDPOINT)
                .content(libraryEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(expectedErrorMessage));
    }

    @Test
    void postLibraryEventTestWithFakeBook() throws Exception {
        // given a library event with fake book
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(new Book())
                .build();
        String libraryEventJson = objectMapper.writeValueAsString(libraryEvent);

        /** This is a unit test so we mock the behavior of the sendLibraryEventWithProducerRecord()
         *  method of the LibraryEventProducer. */
        when(libraryEventProducerService.sendLibraryEventWithProducerRecord(isA(LibraryEvent.class))).thenReturn(null);

        // when
        String expectedErrorMessage = "book.bookAuthor - must not be blank, book.bookId - must not be null, book.bookName - must not be blank";
        mockMvc.perform(post(LIBRARY_V1_ENDPOINT)
                .content(libraryEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(expectedErrorMessage));
    }

    @Test
    void postLibraryEventTestBookWithoutName() throws Exception {
        // given a library event with no book
        Book book = Book.builder()
                .bookId(584)
                .bookAuthor("")
                .bookName("")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();
        String libraryEventJson = objectMapper.writeValueAsString(libraryEvent);

        /** This is a unit test so we mock the behavior of the sendLibraryEventWithProducerRecord()
         *  method of the LibraryEventProducer. */
        when(libraryEventProducerService.sendLibraryEventWithProducerRecord(isA(LibraryEvent.class))).thenReturn(null);

        // when
        String expectedErrorMessage = "book.bookAuthor - must not be blank, book.bookName - must not be blank";
        mockMvc.perform(post(LIBRARY_V1_ENDPOINT)
                .content(libraryEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(expectedErrorMessage));
    }

    @Test
    void postLibraryEventTestBookWithoutId() throws Exception {
        // given a library event with no book
        Book book = Book.builder()
                .bookId(null)
                .bookAuthor("Oscar Wilde")
                .bookName("The portrait of Dorian Gray")
                .build();
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .book(book)
                .build();
        String libraryEventJson = objectMapper.writeValueAsString(libraryEvent);

        /** This is a unit test so we mock the behavior of the sendLibraryEventWithProducerRecord()
         *  method of the LibraryEventProducer. */
        when(libraryEventProducerService.sendLibraryEventWithProducerRecord(isA(LibraryEvent.class))).thenReturn(null);

        // when
        String expectedErrorMessage = "book.bookId - must not be null";
        mockMvc.perform(post(LIBRARY_V1_ENDPOINT)
                .content(libraryEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(expectedErrorMessage));
    }

    @Test
    void putLibraryEventTest() throws Exception {
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
        String libraryEventJson = objectMapper.writeValueAsString(libraryEvent);

        /** This is a unit test so we mock the behavior of the sendLibraryEventWithProducerRecord()
         *  method of the LibraryEventProducer. */
        when(libraryEventProducerService.sendLibraryEventWithProducerRecord(isA(LibraryEvent.class))).thenReturn(null);

        // when
        mockMvc.perform(put(LIBRARY_V1_ENDPOINT)
                .content(libraryEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void putLibraryEventTest_withNullLibraryEventId() throws Exception {
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
        when(libraryEventProducerService.sendLibraryEventWithProducerRecord(isA(LibraryEvent.class))).thenReturn(null);

        // when
        mockMvc.perform(put(LIBRARY_V1_ENDPOINT)
                .content(libraryEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
