package com.github.felipegutierrez.explore.spring.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import com.github.felipegutierrez.explore.spring.domain.LibraryEventType;
import com.github.felipegutierrez.explore.spring.services.LibraryEventProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_ERROR_ID_NULL;
import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_REACT_V1_ENDPOINT;

/**
 * Testing based on the project:
 * <p>
 * https://github.com/svgagan/server-sent-events
 */
// @EnableAutoConfiguration
@RestController
@Slf4j
public class LibraryEventsReactController {

    @Autowired
    LibraryEventProducerService libraryEventProducerService;

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

        libraryEventProducerService.sendLibraryEventWithProducerRecord(libraryEvent);

        return Mono.just(new ResponseEntity<>(libraryEvent, HttpStatus.CREATED))
                .log();
    }

    /**
     * test on CLI using:
     * "http POST http://localhost:8080/v1/react/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-00.json"
     * "http PUT  http://localhost:8080/v1/react/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-01.json"
     * "http PUT  http://localhost:8080/v1/react/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-02.json"
     *
     * <p>
     * consume on the Kafka broker using:
     * "./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic library-events --from-beginning --property "print.key=true""
     *
     * @param libraryEvent
     * @return
     * @throws JsonProcessingException
     */
    @PutMapping(LIBRARY_REACT_V1_ENDPOINT)
    public Mono<ResponseEntity> putLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        log.info("received PUT reactive request: {}", libraryEvent);

        if (libraryEvent.getLibraryEventId() == null) {
            return Mono
                    .just(new ResponseEntity(LIBRARY_ERROR_ID_NULL, HttpStatus.BAD_REQUEST))
                    .log();
        }
        libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);

        // invoke the kafka producer and send message asynchronously
        libraryEventProducerService.sendLibraryEventWithProducerRecord(libraryEvent);

        return Mono.just(libraryEvent)
                .map(event -> new ResponseEntity(event, HttpStatus.OK))
                .log();
    }
}
