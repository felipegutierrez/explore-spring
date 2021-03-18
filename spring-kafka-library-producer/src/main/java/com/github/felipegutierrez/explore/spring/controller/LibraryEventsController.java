package com.github.felipegutierrez.explore.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import com.github.felipegutierrez.explore.spring.producer.LibraryEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_V1_ENDPOINT;

@RestController
@Slf4j
public class LibraryEventsController {

    @Autowired
    LibraryEventProducer libraryEventProducer;

    /**
     * test on CLI using:
     * "http POST http://localhost:8080/v1/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-00.json"
     * <p>
     * consume on the Kafka broker using:
     * "./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic library-events --from-beginning --property "print.key=true""
     *
     * @param libraryEvent
     * @return
     */
    @PostMapping(LIBRARY_V1_ENDPOINT)
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
        log.info("received request: {}", libraryEvent);

        // invoke the kafka producer
        libraryEventProducer.sendLibraryEvent(libraryEvent);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(libraryEvent);
    }
}
