package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.EmployeeListenerBinding;
import com.github.felipegutierrez.explore.spring.model.Employee;
import com.github.felipegutierrez.explore.spring.utils.CustomSerdes;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@EnableBinding(EmployeeListenerBinding.class)
public class EmployeeStreamListener {

    @Autowired
    RecordBuilder recordBuilder;

    @StreamListener("employee-input-channel")
    public void process(KStream<String, Employee> input) {
        input.peek((k, v) -> log.info("Key: {}, Value:{}", k, v))
                .map((k, v) -> new KeyValue<>(v.getDepartment(), v))
                .groupByKey(Grouped.with(CustomSerdes.String(), CustomSerdes.EmployeeAvro()))
                .aggregate(
                        () -> recordBuilder.initDepartmentAggregate(),
                        (k, v, aggV) -> recordBuilder.aggregateDepartmentAggregate(v, aggV),
                        Materialized.with(CustomSerdes.String(), CustomSerdes.DepartmentAggregateAvro())
                ).toStream()
                .foreach((k, v) -> log.info("Key = " + k + " Value = " + v.toString()));
    }
}
