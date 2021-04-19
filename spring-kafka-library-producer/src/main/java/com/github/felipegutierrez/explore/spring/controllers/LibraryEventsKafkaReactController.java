package com.github.felipegutierrez.explore.spring.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.felipegutierrez.explore.spring.domain.LibraryEvent;
import com.github.felipegutierrez.explore.spring.domain.LibraryEventType;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import javax.validation.Valid;
import java.util.List;

import static com.github.felipegutierrez.explore.spring.util.LibraryProducerConstants.LIBRARY_REACTIVE_V1_ENDPOINT;
import static com.github.felipegutierrez.explore.spring.util.LibraryProducerConstants.LIBRARY_V1_TOPIC;

/**
 * Testing based on the project:
 * <p>
 * https://github.com/svgagan/server-sent-events
 */
// @EnableAutoConfiguration
@RestController
@Slf4j
public class LibraryEventsKafkaReactController {

    // @Autowired
    // @Qualifier("libraryEventKafkaProducer")
    KafkaSender<Integer, String> kafkaSender;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * test on CLI using:
     * "http POST http://localhost:8080/v1/reactive/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-00.json"
     * <p>
     * consume on the Kafka broker using:
     * "./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic library-events --from-beginning --property "print.key=true""
     *
     * @param libraryEvent
     * @return
     */
    @PostMapping(value = LIBRARY_REACTIVE_V1_ENDPOINT)
    public @ResponseBody
    Mono<LibraryEvent> postLibraryEventReact(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        log.info("received POST reactive request: {}", libraryEvent);

        return Mono.just(libraryEvent)
                .map(le -> {
                    le.setLibraryEventType(LibraryEventType.NEW);

                    Integer key = le.getLibraryEventId();
                    String value = null;
                    try {
                        value = objectMapper.writeValueAsString(le);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    ProducerRecord<Integer, String> producerRecord = buildProducerRecord(LIBRARY_V1_TOPIC, key, value);
                    Mono<SenderRecord<Integer, String, String>> senderRecordMono = Mono.just(SenderRecord.create(producerRecord, null));
                    return kafkaSender.send(senderRecordMono);
                })
                .flatMap(m -> Mono.just(libraryEvent));
    }

    private ProducerRecord<Integer, String> buildProducerRecord(String topic, Integer key, String value) {
        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<Integer, String>(topic, null, key, value, recordHeaders);
    }
}
