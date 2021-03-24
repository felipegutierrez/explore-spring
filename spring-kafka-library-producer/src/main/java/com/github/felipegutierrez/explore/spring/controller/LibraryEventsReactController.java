package com.github.felipegutierrez.explore.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import com.github.felipegutierrez.explore.spring.domain.LibraryEventType;
import com.github.felipegutierrez.explore.spring.producer.LibraryEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_FUNC_V1_ENDPOINT;

/**
 * Testing based on the project:
 *
 * https://github.com/svgagan/server-sent-events
 */
// @EnableAutoConfiguration
@RestController
@Slf4j
public class LibraryEventsReactController {

    @Autowired
    LibraryEventProducer libraryEventProducer;

    @PostMapping(value = LIBRARY_FUNC_V1_ENDPOINT)
    public Mono<ResponseEntity<LibraryEvent>> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) {
        log.info("received POST reactive request: {}", libraryEvent);

        libraryEvent.setLibraryEventType(LibraryEventType.NEW);

        return Mono.just(libraryEvent)
                .map(event -> new ResponseEntity<>(event, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND))
                .log();
    }
}
