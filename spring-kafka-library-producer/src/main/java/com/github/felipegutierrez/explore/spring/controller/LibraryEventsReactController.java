package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import static com.github.felipegutierrez.explore.spring.util.LibraryConstants.LIBRARY_REACT_V1_ENDPOINT;

/**
 * Testing based on the project:
 *
 * https://github.com/svgagan/server-sent-events
 */
@EnableAutoConfiguration
@RestController
@Slf4j
public class LibraryEventsReactController {

    /*
    @Autowired
    KafkaReceiver<Integer, String> kafkaReceiver;
     */

    /**
     * test on CLI using:
     * "http GET http://localhost:8080/v1/react/libraryevent"
     *
     * @param libraryEvent
     * @return
     */
    /*
    @GetMapping(value = LIBRARY_REACT_V1_ENDPOINT, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) {
        log.info("received reactive request: {}", libraryEvent);

        // invoke the kafka producer

        Flux<ReceiverRecord<Integer, String>> kafkaFlux = kafkaReceiver.receive();

        return kafkaFlux
                .log()
                // .map(ReceiverRecord::value)
                .ofType(LibraryEvent.class)
                ;
    }
    */
}
