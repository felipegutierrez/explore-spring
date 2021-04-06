package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.WordListenerBinding;
import com.github.felipegutierrez.explore.spring.utils.CustomSerdes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@EnableBinding(WordListenerBinding.class)
public class WordListenerService {

    @StreamListener("words-input-channel")
    @SendTo("words-output-channel")
    public KStream<String, String> process(KStream<String, String> input) {
        log.info("received message on words-input-channel channel: {}", input);

        return input
                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split(" ")))
                .groupBy((key, value) -> value, Grouped.with(CustomSerdes.String(), CustomSerdes.String()))
                .count(Materialized.with(CustomSerdes.String(), CustomSerdes.Long()))
                .mapValues(value -> String.valueOf(value), Materialized.with(CustomSerdes.String(), CustomSerdes.String()))
                .toStream()
                .peek((k, v) -> log.info("Word: [{}] Count: [{}]", k, v))
                ;
        // .to(WORDS_STREAMING_OUTPUT_TOPIC_NAME, Produced.with(CustomSerdes.String(), CustomSerdes.String()));
    }
}
