package com.github.felipegutierrez.explore.spring.bindings;

import com.github.felipegutierrez.explore.spring.model.AdClick;
import com.github.felipegutierrez.explore.spring.model.AdInventories;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface ClicksListenerBinding {

    @Input("inventories-channel")
    GlobalKTable<String, AdInventories> inventoryInputStream();

    @Input("clicks-channel")
    KStream<String, AdClick> clickInputStream();
}
