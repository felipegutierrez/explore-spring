package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.EmployeeListenerBinding;
import com.github.felipegutierrez.explore.spring.model.Employee;
import com.github.felipegutierrez.explore.spring.model.Order;
import com.github.felipegutierrez.explore.spring.utils.CustomSerdes;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Configuration
@EnableAutoConfiguration
public class EmployeeStreamFunctionalListener {

    @Autowired
    RecordBuilder recordBuilder;

    @Bean
    public Function<KStream<String, Employee>, KStream<String, String>> streamAggEmployeesByDepartment() {
        /** The KStream does not remove values. Its values are immutable.
         * in order to update values we have to use the aggregate transformation with
         * the subtractor operation from KTable. */
        /*
        input.peek((k, v) -> log.info("Key: {}, Value:{}", k, v))
                .map((k, v) -> new KeyValue<>(v.getDepartment(), v))
                .groupByKey(Grouped.with(CustomSerdes.String(), CustomSerdes.EmployeeAvro()))
                .aggregate(
                        () -> recordBuilder.initDepartmentAggregate(),
                        (k, v, aggV) -> recordBuilder.aggregateDepartmentAggregate(v, aggV),
                        Materialized.with(CustomSerdes.String(), CustomSerdes.DepartmentAggregateAvro())
                ).toStream()
                .foreach((k, v) -> log.info("Key = " + k + " Value = " + v.toString()));
         */

        /*
        input.peek((k, v) -> log.info("Key: {}, Value:{}", k, v))
                .map((k, v) -> new KeyValue<>(v.getDepartment(), v))
                .toTable(Materialized.with(CustomSerdes.String(), CustomSerdes.EmployeeAvro()))
                .groupBy((k, v) -> KeyValue.pair(v.getDepartment(), v), Grouped.with(CustomSerdes.String(), CustomSerdes.EmployeeAvro()))
                .aggregate(
                        () -> recordBuilder.initDepartmentAggregate(),
                        (k, v, aggV) -> recordBuilder.addedDepartmentAggregate(v, aggV),
                        (k, v, aggV) -> recordBuilder.subtractDepartmentAggregate(v, aggV),
                        Materialized.with(CustomSerdes.String(), CustomSerdes.DepartmentAggregateAvro())
                ).toStream()
                .foreach((k, v) -> log.info("Key = " + k + " Value = " + v.toString()));
         */
        return input -> input
                .peek((k, v) -> log.info("Key: {}, Value:{}", k, v))
                .mapValues(value -> value.toString());
    }
}
