package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.ClicksListenerBinding;
import com.github.felipegutierrez.explore.spring.model.AdClick;
import com.github.felipegutierrez.explore.spring.model.AdInventories;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableBinding(ClicksListenerBinding.class)
public class ClickListenerService {
    @StreamListener
    public void process(@Input("inventories-channel") GlobalKTable<String, AdInventories> inventory,
                        @Input("clicks-channel") KStream<String, AdClick> click) {

        click.foreach((k, v) -> log.info("Click Key: {}, Value: {}", k, v));

        click.join(inventory,
                (adClick, adClickValue) -> adClick,
                (adClickValue, adInventories) -> adInventories)
                .groupBy((joinedKey, joinedValue) -> joinedValue.getNewsType(),
                        Grouped.with(Serdes.String(), new JsonSerde<>(AdInventories.class))
                )
                .count()
                .toStream()
                .foreach((k, v) -> log.info("Click Key: {}, Value: {}", k, v));
    }
}
