package com.github.felipegutierrez.explore.spring.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@Component
public class WordListenerHandler {

    private final String WORDS_STREAMING_OUTPUT_TOPIC_NAME = "streaming-words-output-topic";

    Queue<String> stringStream = new LinkedList<String>();

    public Mono<ServerResponse> fluxWordCountStream(ServerRequest serverRequest) {

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(Flux.fromStream(stringStream.stream()), String.class);
        // .body(Flux.interval(Duration.ofSeconds(1)).log(), Long.class);
    }

    @KafkaListener(topics = {WORDS_STREAMING_OUTPUT_TOPIC_NAME})
    public void onMessage(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        log.info("ConsumerRecord received: {}", consumerRecord);

        String message = consumerRecord.key() + " : " + consumerRecord.value() + "\n";
        stringStream.add(message);
    }
}
