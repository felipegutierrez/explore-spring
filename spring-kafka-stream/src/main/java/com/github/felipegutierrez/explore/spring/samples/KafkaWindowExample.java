package com.github.felipegutierrez.explore.spring.samples;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Reducer;

public class KafkaWindowExample {

    public Topology getKafkaStreamWindow() {
        final StreamsBuilder builder = new StreamsBuilder();
        KStream<Long, String> stream = builder.stream("INPUT_TOPIC", Consumed.with(Serdes.Long(), Serdes.String()));
        stream
                .mapValues(value -> Long.valueOf(value))
                .groupByKey()
                .reduce(new Reducer<Long>() {
                    @Override
                    public Long apply(Long currentMax, Long v) {
                        return (currentMax > v) ? currentMax : v;
                    }
                })
                .toStream().to("OUTPUT_TOPIC");
        return builder.build();
    }
}
