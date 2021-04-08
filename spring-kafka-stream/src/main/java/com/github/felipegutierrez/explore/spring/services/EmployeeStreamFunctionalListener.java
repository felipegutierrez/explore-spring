package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.model.DepartmentAggregate;
import com.github.felipegutierrez.explore.spring.model.Employee;
import com.github.felipegutierrez.explore.spring.utils.CustomSerdes;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;

@Slf4j
@Configuration
@EnableAutoConfiguration
public class EmployeeStreamFunctionalListener {

    private final Serde<String> stringSerde;
    private final Serde<Employee> employeeSerde;
    private final Serde<DepartmentAggregate> departmentAggregateSerde;

    @Autowired
    RecordBuilder recordBuilder;

    public EmployeeStreamFunctionalListener(@Value("${application.configs.serdes.scope.test}") Boolean scopeTest) {
        if (scopeTest) {
            this.stringSerde = Serdes.String();
            this.employeeSerde = new SpecificAvroSerde<>();
            this.departmentAggregateSerde = new SpecificAvroSerde<>();

            String SCHEMA_REGISTRY_SCOPE = EmployeeStreamFunctionalListener.class.getName();
            String MOCK_SCHEMA_REGISTRY_URL = "mock://" + SCHEMA_REGISTRY_SCOPE;
            Map<String, String> config = Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MOCK_SCHEMA_REGISTRY_URL);
            employeeSerde.configure(config, false);
            departmentAggregateSerde.configure(config, false);
        } else {
            this.stringSerde = CustomSerdes.String();
            this.employeeSerde = CustomSerdes.EmployeeAvro();
            this.departmentAggregateSerde = CustomSerdes.DepartmentAggregateAvro();
        }
    }

    @Bean
    public Function<KStream<String, Employee>, KStream<String, DepartmentAggregate>> streamAggEmployeesByDepartment() {
        /** The KStream does not remove values. Its values are immutable.
         * in order to update values we have to use the aggregate transformation with
         * the subtractor operation from KTable. */
        return input -> input.peek((k, v) -> log.info("Employee - Key: {}, Value:{}", k, v))
                .map((k, v) -> new KeyValue<>(v.getDepartment(), v))
                // .toTable(Materialized.with(stringSerde, employeeSerde))
                // .groupBy((k, v) -> KeyValue.pair(v.getDepartment(), v), Grouped.with(stringSerde, employeeSerde))
                .groupByKey(Grouped.with(stringSerde, employeeSerde))
                .aggregate(
                        () -> recordBuilder.initDepartmentAggregate(),
                        (k, v, aggV) -> recordBuilder.addedDepartmentAggregate(v, aggV),
                        // (k, v, aggV) -> aggV, // recordBuilder.subtractDepartmentAggregate(v, aggV),
                        Materialized.with(stringSerde, departmentAggregateSerde)
                )
                .toStream()
                .peek((k, v) -> log.info("DepartmentAggregate - Key: {}, Value:{}", k, v.toString()));
    }
}
