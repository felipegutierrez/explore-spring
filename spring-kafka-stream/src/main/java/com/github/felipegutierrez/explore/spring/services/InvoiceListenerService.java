package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.InvoiceListenerBinding;
import com.github.felipegutierrez.explore.spring.model.SimpleInvoice;
import com.github.felipegutierrez.explore.spring.utils.CustomSerdes;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

@Log4j2
@Service
@EnableBinding(InvoiceListenerBinding.class)
public class InvoiceListenerService {

    @StreamListener("invoice-input-channel")
    public void process(KStream<String, SimpleInvoice> input) {
        input.peek((k, v) -> log.info("Key = " + k + " Created Time = "
                + Instant.ofEpochMilli(v.getCreatedTime()).atOffset(ZoneOffset.UTC)))
                .groupByKey(Grouped.with(CustomSerdes.String(), CustomSerdes.SimpleInvoice()))
                .windowedBy(TimeWindows.of(Duration.ofSeconds(30)))
                .count()
                .toStream()
                .foreach((k, v) ->
                        log.info("StoreID: " + k.key() +
                                " Window start: " + Instant.ofEpochMilli(k.window().start()).atOffset(ZoneOffset.UTC) +
                                " Window end: " + Instant.ofEpochMilli(k.window().end()).atOffset(ZoneOffset.UTC) +
                                " Count: " + v + " Window#: " + k.window().hashCode())
                );
    }
}
