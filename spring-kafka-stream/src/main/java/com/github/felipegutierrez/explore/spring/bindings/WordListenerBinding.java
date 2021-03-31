package com.github.felipegutierrez.explore.spring.bindings;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface WordListenerBinding {

    @Input("words-input-channel")
    KStream<String, String> wordsInputStream();
}
