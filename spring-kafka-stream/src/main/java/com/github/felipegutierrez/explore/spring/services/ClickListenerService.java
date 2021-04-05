package com.github.felipegutierrez.explore.spring.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.felipegutierrez.explore.spring.bindings.ClicksListenerBinding;
import com.github.felipegutierrez.explore.spring.model.AdClick;
import com.github.felipegutierrez.explore.spring.model.AdClicksByNewsType;
import com.github.felipegutierrez.explore.spring.model.AdInventories;
import com.github.felipegutierrez.explore.spring.model.AdTop3NewsTypes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
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

        /** KStream JOIN and counting */
        /*
        click.join(inventory,
                (adClick, adClickValue) -> adClick,
                (adClickValue, adInventories) -> adInventories)
                .groupBy((joinedKey, joinedValue) -> joinedValue.getNewsType(),
                        Grouped.with(Serdes.String(), new JsonSerde<>(AdInventories.class))
                )
                .count()
                .toStream()
                .foreach((k, v) -> log.info("Click Key: {}, Value: {}", k, v));
         */

        /** KStream JOIN and TOP-3 counting */
        KTable<String, Long> clicksByNewsTypeKTable = click.join(inventory,
                (adClick, adClickValue) -> adClick,
                (adClickValue, adInventories) -> adInventories)
                .groupBy((joinedKey, joinedValue) -> joinedValue.getNewsType(),
                        Grouped.with(Serdes.String(), new JsonSerde<>(AdInventories.class))
                )
                .count();

        clicksByNewsTypeKTable.groupBy((key, value) -> {
                    AdClicksByNewsType newValue = new AdClicksByNewsType();
                    newValue.setNewsType(key);
                    newValue.setClicks(value);
                    return KeyValue.pair("top3NewsType", newValue);
                },
                Grouped.with(Serdes.String(), new JsonSerde<>(AdClicksByNewsType.class)))
                .aggregate(
                        () -> new AdTop3NewsTypes(), // Agg initializer
                        (key, value, oldAgg) -> { // Agg adder
                            oldAgg.add(value);
                            return oldAgg;
                        },
                        (key, value, oldAgg) -> { // Agg subtracter
                            oldAgg.remove(value);
                            return oldAgg;
                        },
                        Materialized.<String, AdTop3NewsTypes, KeyValueStore<Bytes, byte[]>> // Agg serializer
                                as("top3-clicks")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(AdTop3NewsTypes.class))
                )
                .toStream()
                .foreach((k, v) -> {
                    try {
                        log.info("k=" + k + " v= " + v.getTop3Sorted());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
    }
}
