package com.github.felipegutierrez.explore.spring.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import com.github.felipegutierrez.explore.spring.domain.LibraryEventType;
import com.github.felipegutierrez.explore.spring.services.LibraryEventAvroProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.github.felipegutierrez.explore.spring.util.LibraryProducerConstants.*;

@RestController
@Slf4j
public class LibraryEventAvroController {

    @Autowired
    LibraryEventAvroProducerService libraryEventAvroProducerService;

    /**
     * test on CLI using:
     * "http POST http://localhost:8080/v1/libraryevent/avro < spring-kafka-library-producer/src/main/resources/static/libraryEvent-00.json"
     * <p>
     * consume on the Kafka broker using:
     * "./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic library-events --from-beginning --property "print.key=true""
     *
     * @param libraryEvent
     * @return
     */
    @PostMapping(LIBRARY_AVRO_V1_ENDPOINT)
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        log.info("received POST request: {}", libraryEvent);

        libraryEvent.setLibraryEventType(LibraryEventType.NEW);

        // invoke the kafka producer and send message asynchronously
        libraryEventAvroProducerService.sendLibraryEventWithProducerRecord(libraryEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    /**
     * test on CLI using:
     * "http POST http://localhost:8080/v1/sync/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-00.json"
     * <p>
     * consume on the Kafka broker using:
     * "./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic library-events --from-beginning --property "print.key=true""
     *
     * @param libraryEvent
     * @return
     */
    @PostMapping(LIBRARY_AVRO_V1_SYNC_ENDPOINT)
    public ResponseEntity<LibraryEvent> postLibraryEventSync(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {
        log.info("received POST sync request: {}", libraryEvent);

        libraryEvent.setLibraryEventType(LibraryEventType.NEW);

        // invoke the kafka producer and send message synchronously
        // SendResult<Integer, String> sendResult = libraryEventAvroProducer.sendLibraryEventBlocking(libraryEvent);
        // log.info("message sent: {}", sendResult);

        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    /**
     * test on CLI using:
     * "http POST http://localhost:8080/v1/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-00.json"
     * "http PUT  http://localhost:8080/v1/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-01.json"
     * "http PUT  http://localhost:8080/v1/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-02.json"
     *
     * <p>
     * consume on the Kafka broker using:
     * "./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic library-events --from-beginning --property "print.key=true""
     *
     * @param libraryEvent
     * @return
     * @throws JsonProcessingException
     */
    @PutMapping(LIBRARY_AVRO_V1_ENDPOINT)
    public ResponseEntity<?> putLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        log.info("received PUT request: {}", libraryEvent);

        if (libraryEvent.getLibraryEventId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(LIBRARY_ERROR_ID_NULL);
        }
        libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);

        // invoke the kafka producer and send message asynchronously
        // libraryEventAvroProducer.sendLibraryEventWithProducerRecord(libraryEvent);

        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }
}
