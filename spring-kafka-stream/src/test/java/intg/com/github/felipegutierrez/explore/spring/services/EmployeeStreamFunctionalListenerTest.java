package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.config.KafkaTopicsConfig;
import com.github.felipegutierrez.explore.spring.model.DepartmentAggregate;
import com.github.felipegutierrez.explore.spring.model.Employee;
import io.confluent.kafka.schemaregistry.testutil.MockSchemaRegistry;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {"server.port=0"})
@ExtendWith(SpringExtension.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1)
public class EmployeeStreamFunctionalListenerTest {

    private static final String SCHEMA_REGISTRY_SCOPE = EmployeeStreamFunctionalListenerTest.class.getName();
    private static final String MOCK_SCHEMA_REGISTRY_URL = "mock://" + SCHEMA_REGISTRY_SCOPE;
    @Autowired
    KafkaTopicsConfig kafkaTopicsConfig;
    @Autowired
    EmployeeStreamFunctionalListener employeeStreamFunctionalListener;
    Serde<String> stringSerde = Serdes.String();
    Serde<Employee> employeeSerde = new SpecificAvroSerde<>();
    Serde<DepartmentAggregate> departmentAggregateSerde = new SpecificAvroSerde<>();
    private TestInputTopic<String, Employee> employeeTopic;
    private TestOutputTopic<String, DepartmentAggregate> departmentAggregateTopic;
    private String INPUT_TOPIC;
    private String OUTPUT_TOPIC;
    private TopologyTestDriver testDriver;
    private TestInputTopic inputTopic;
    private TestOutputTopic outputTopic;

    private Employee employee101;
    private Employee employee102;
    private Employee employee103;

    static Properties getStreamsConfiguration() {
        final Properties streamsConfiguration = new Properties();
        // Need to be set even these do not matter with TopologyTestDriver
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "TopologyTestDriver");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        streamsConfiguration.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MOCK_SCHEMA_REGISTRY_URL);
        return streamsConfiguration;
    }

    @BeforeEach
    public void setUp() {
        employee101 = Employee.newBuilder().setId("101").setName("Prashant").setDepartment("engineering").setSalary(5000).build();
        employee102 = Employee.newBuilder().setId("102").setName("John").setDepartment("accounts").setSalary(8000).build();
        employee103 = Employee.newBuilder().setId("103").setName("Abdul").setDepartment("engineering").setSalary(3000).build();

        INPUT_TOPIC = kafkaTopicsConfig.getEMPLOYEE_FUNC_INPUT_TOPIC_NAME();
        OUTPUT_TOPIC = kafkaTopicsConfig.getEMPLOYEE_FUNC_OUTPUT_TOPIC_NAME();

        Map<String, String> config = Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MOCK_SCHEMA_REGISTRY_URL);
        employeeSerde.configure(config, false);
        departmentAggregateSerde.configure(config, false);

        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Employee> input = builder.stream(INPUT_TOPIC, Consumed.with(stringSerde, employeeSerde));
        final Function<KStream<String, Employee>, KStream<String, DepartmentAggregate>> process = employeeStreamFunctionalListener.streamAggEmployeesByDepartment();
        final KStream<String, DepartmentAggregate> output = process.apply(input);
        output.to(OUTPUT_TOPIC, Produced.with(stringSerde, departmentAggregateSerde));

        testDriver = new TopologyTestDriver(builder.build(), getStreamsConfiguration());
        inputTopic = testDriver.createInputTopic(INPUT_TOPIC, stringSerde.serializer(), employeeSerde.serializer());
        outputTopic = testDriver.createOutputTopic(OUTPUT_TOPIC, stringSerde.deserializer(), departmentAggregateSerde.deserializer());
    }

    @AfterEach
    public void tearDown() {
        testDriver.close();
        MockSchemaRegistry.dropScope(SCHEMA_REGISTRY_SCOPE);
    }

    @Test
    public void testOneEmployee() {
        inputTopic.pipeInput(employee101);

        DepartmentAggregate departmentAggregateExpected = DepartmentAggregate.newBuilder()
                .setEmployeeCount(1)
                .setTotalSalary(5000)
                .setAvgSalary(5000.0)
                .build();

        // Read and validate output
        final DepartmentAggregate outputDepartmentAggregate = (DepartmentAggregate) outputTopic.readValue();
        assertThat(outputDepartmentAggregate).isNotNull();

        System.out.println("outputDepartmentAggregate");
        System.out.println(outputDepartmentAggregate.getTotalSalary());
        System.out.println(outputDepartmentAggregate.getEmployeeCount());
        System.out.println(outputDepartmentAggregate.getAvgSalary());
        assertThat(outputDepartmentAggregate).usingRecursiveComparison().isEqualTo(departmentAggregateExpected);

        //No more output in topic
        assertThat(outputTopic.isEmpty()).isTrue();
    }

    @Test
    public void testTwoEmployee() throws InterruptedException {
        inputTopic.pipeInput(employee101);
        inputTopic.pipeInput(employee103);
        Thread.sleep(1000);

        DepartmentAggregate departmentAggregateEngineeringExpected00 = DepartmentAggregate.newBuilder()
                .setEmployeeCount(1).setTotalSalary(5000).setAvgSalary(5000.0).build();
        DepartmentAggregate departmentAggregateEngineeringExpected01 = DepartmentAggregate.newBuilder()
                .setEmployeeCount(2).setTotalSalary(8000).setAvgSalary(4000.0).build();

        final List<DepartmentAggregate> departmentAggregateList = (List<DepartmentAggregate>) outputTopic.readValuesToList();
        assertThat(departmentAggregateList).isNotNull();
        System.out.println("departmentAggregateList");
        departmentAggregateList.forEach(System.out::println);

        assertThat(departmentAggregateList.get(0)).usingRecursiveComparison().isEqualTo(departmentAggregateEngineeringExpected00);
        assertThat(departmentAggregateList.get(1)).usingRecursiveComparison().isEqualTo(departmentAggregateEngineeringExpected01);

        //No more output in topic
        assertThat(outputTopic.isEmpty()).isTrue();
    }
}
