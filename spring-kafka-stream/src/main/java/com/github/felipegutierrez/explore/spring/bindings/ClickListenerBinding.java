package com.github.felipegutierrez.explore.spring.bindings;

import com.github.felipegutierrez.explore.spring.model.UserClick;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface ClickListenerBinding {

    @Input("click-input-channel")
    KStream<String, UserClick> clickInputStream();
}
