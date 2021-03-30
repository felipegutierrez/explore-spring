package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.StockListenerBinding;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableBinding(StockListenerBinding.class)
public class StockTickListenerService {

    @StreamListener("stock-input-channel")
    public void process(KTable<String, String> input) {

        input.filter((key, value) -> key.contains("HDFCBANK"))
                .toStream()
                .foreach((k, v) -> System.out.println("Key = " + k + " Value = " + v));
    }
}
