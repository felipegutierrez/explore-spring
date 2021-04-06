package com.github.felipegutierrez.explore.spring.bindings;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface WordListenerBinding {

    @Input("words-input-channel")
    KStream<String, String> wordsInputStream();

    @Output("words-output-channel")
    KStream<String, String> wordsOutputStream();
}
