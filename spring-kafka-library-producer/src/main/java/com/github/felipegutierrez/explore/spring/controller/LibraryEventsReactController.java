package com.github.felipegutierrez.explore.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import com.github.felipegutierrez.explore.spring.domain.LibraryEventType;
import com.github.felipegutierrez.explore.spring.producer.LibraryEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.*;

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

    /**
     * test on CLI using:
     * "http POST http://localhost:8080/v1/react/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-00.json"
     * <p>
     * consume on the Kafka broker using:
     * "./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic library-events --from-beginning --property "print.key=true""
     *
     * @param libraryEvent
     * @return
     */
    @PostMapping(value = LIBRARY_REACT_V1_ENDPOINT)
    public Mono<ResponseEntity<LibraryEvent>> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        log.info("received POST reactive request: {}", libraryEvent);

        libraryEvent.setLibraryEventType(LibraryEventType.NEW);

        libraryEventProducer.sendLibraryEventWithProducerRecord(libraryEvent);

        return Mono.just(libraryEvent)
                .map(event -> new ResponseEntity<>(event, HttpStatus.CREATED))
                .log();
    }
}
