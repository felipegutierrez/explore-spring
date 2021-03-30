package com.github.felipegutierrez.explore.spring.bindings;

import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;

public interface StockListenerBinding {

    @Input("stock-input-channel")
    KTable<String, String> stockInputStream();
}
