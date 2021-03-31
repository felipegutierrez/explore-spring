package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.WordListenerBinding;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@EnableBinding(WordListenerBinding.class)
public class WordListenerService {

    @StreamListener("words-input-channel")
    public void process(KStream<String, String> input) {
        log.info("received message on words-input-channel channel: {}", input);

        KStream<String, String> wordStream = input
                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split(" ")));

        // KStream<String, String> aggregateStream =
        wordStream.groupBy((key, value) -> value)
                .count()
                .mapValues(value -> String.valueOf(value))
                .toStream()
                .peek((k, v) -> log.info("Word: [{}] Count: [{}]", k, v));
    }
}
