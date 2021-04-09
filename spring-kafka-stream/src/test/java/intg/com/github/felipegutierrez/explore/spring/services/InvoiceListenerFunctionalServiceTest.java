package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.config.KafkaTopicsConfig;
import com.github.felipegutierrez.explore.spring.model.SimpleInvoice;
import io.confluent.kafka.schemaregistry.testutil.MockSchemaRegistry;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.json.KafkaJsonSchemaSerde;
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
import org.springframework.kafka.support.serializer.JsonSerde;
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
public class InvoiceListenerFunctionalServiceTest {

    private static final String SCHEMA_REGISTRY_SCOPE = EmployeeStreamFunctionalListenerTest.class.getName();
    private static final String MOCK_SCHEMA_REGISTRY_URL = "mock://" + SCHEMA_REGISTRY_SCOPE;
    @Autowired
    KafkaTopicsConfig kafkaTopicsConfig;
    @Autowired
    InvoiceListenerFunctionalService invoiceListenerFunctionalService;
    Serde<String> stringSerde = Serdes.String();
    Serde<SimpleInvoice> simpleInvoiceSerde = new KafkaJsonSchemaSerde<>();
    private TestInputTopic<String, SimpleInvoice> simpleInvoiceTopic;
    private TestOutputTopic<String, String> outputTopic;
    private String INPUT_TOPIC;
    private String OUTPUT_TOPIC;
    private TopologyTestDriver testDriver;

    private SimpleInvoice simpleInvoice01;
    private SimpleInvoice simpleInvoice02;
    private SimpleInvoice simpleInvoice03;

    static Properties getStreamsConfiguration() {
        final Properties streamsConfiguration = new Properties();
        // Need to be set even these do not matter with TopologyTestDriver
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "TopologyTestDriver");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
        streamsConfiguration.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MOCK_SCHEMA_REGISTRY_URL);
        return streamsConfiguration;
    }

    @BeforeEach
    public void setUp() {
        // STR1534:{"InvoiceNumber": 101,"CreatedTime": "1549360860000","StoreID": "STR1534", "TotalAmount": 1920}
        simpleInvoice01 = new SimpleInvoice();
        simpleInvoice01.setInvoiceNumber("101");
        simpleInvoice01.setCreatedTime(1549360860000l);
        simpleInvoice01.setStoreID("STR1534");
        simpleInvoice01.setTotalAmount(1920.0);

        simpleInvoice02 = new SimpleInvoice();
        simpleInvoice02.setInvoiceNumber("102");
        simpleInvoice02.setCreatedTime(1549360900000l);
        simpleInvoice02.setStoreID("STR1535");
        simpleInvoice02.setTotalAmount(1860.0);

        simpleInvoice03 = new SimpleInvoice();
        simpleInvoice03.setInvoiceNumber("103");
        simpleInvoice03.setCreatedTime(1549360999000l);
        simpleInvoice03.setStoreID("STR1534");
        simpleInvoice03.setTotalAmount(2400.0);

        INPUT_TOPIC = kafkaTopicsConfig.getSIMPLE_INVOICE_FUNC_INPUT_TOPIC_NAME();
        OUTPUT_TOPIC = kafkaTopicsConfig.getSIMPLE_INVOICE_FUNC_OUTPUT_TOPIC_NAME();

        Map<String, String> config = Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MOCK_SCHEMA_REGISTRY_URL);
        simpleInvoiceSerde.configure(config, false);

        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String, SimpleInvoice> input = builder.stream(INPUT_TOPIC, Consumed.with(stringSerde, simpleInvoiceSerde));
        final Function<KStream<String, SimpleInvoice>, KStream<String, String>> process = invoiceListenerFunctionalService.streamSimpleInvoiceCountWindow();
        final KStream<String, String> output = process.apply(input);
        output.to(OUTPUT_TOPIC, Produced.with(stringSerde, stringSerde));

        testDriver = new TopologyTestDriver(builder.build(), getStreamsConfiguration());
        simpleInvoiceTopic = testDriver.createInputTopic(INPUT_TOPIC, stringSerde.serializer(), simpleInvoiceSerde.serializer());
        outputTopic = testDriver.createOutputTopic(OUTPUT_TOPIC, stringSerde.deserializer(), stringSerde.deserializer());
    }

    @AfterEach
    public void tearDown() {
        testDriver.close();
        MockSchemaRegistry.dropScope(SCHEMA_REGISTRY_SCOPE);
    }

    @Test
    public void testOneInvoice() {
        simpleInvoiceTopic.pipeInput(simpleInvoice01.getStoreID(), simpleInvoice01);

        // Read and validate output
        assertThat(outputTopic.isEmpty()).isFalse();
        final String outputInvoiceWindow = outputTopic.readValue();
        assertThat(outputInvoiceWindow).isNotNull();

        System.out.println("outputInvoiceWindow");
        System.out.println(outputInvoiceWindow);
        String[] outputInvoiceWindowArray = outputInvoiceWindow.split(" ");
        System.out.println(outputInvoiceWindowArray[1]);
        System.out.println(outputInvoiceWindowArray[3]);
        assertThat(outputInvoiceWindowArray[1]).isEqualTo(simpleInvoice01.getStoreID());
        assertThat(outputInvoiceWindowArray[3]).isEqualTo("1");

        //No more output in topic
        assertThat(outputTopic.isEmpty()).isTrue();
    }

    @Test
    public void testTwoInvoicesWithSameStoreID() {
        simpleInvoiceTopic.pipeInput(simpleInvoice01.getStoreID(), simpleInvoice01);
        simpleInvoiceTopic.pipeInput(simpleInvoice03.getStoreID(), simpleInvoice03);

        // Read and validate output
        assertThat(outputTopic.isEmpty()).isFalse();
        final List<String> outputInvoiceWindowList = outputTopic.readValuesToList();
        System.out.println("outputInvoiceWindowList");
        outputInvoiceWindowList.forEach(System.out::println);
        assertThat(outputInvoiceWindowList.size()).isEqualTo(2);
        String[] outputInvoiceWindowArray = outputInvoiceWindowList.get(1).split(" ");
        assertThat(outputInvoiceWindowArray[1]).isEqualTo(simpleInvoice01.getStoreID());
        assertThat(outputInvoiceWindowArray[3]).isEqualTo("2");

        //No more output in topic
        assertThat(outputTopic.isEmpty()).isTrue();
    }

    @Test
    public void testThreeInvoicesWithDifferentStoreID() {
        simpleInvoiceTopic.pipeInput(simpleInvoice01.getStoreID(), simpleInvoice01);
        simpleInvoiceTopic.pipeInput(simpleInvoice02.getStoreID(), simpleInvoice02);
        simpleInvoiceTopic.pipeInput(simpleInvoice03.getStoreID(), simpleInvoice03);

        // Read and validate output
        assertThat(outputTopic.isEmpty()).isFalse();
        final List<String> outputInvoiceWindowList = outputTopic.readValuesToList();
        System.out.println("outputInvoiceWindowList");
        outputInvoiceWindowList.forEach(System.out::println);
        assertThat(outputInvoiceWindowList.size()).isEqualTo(3);

        // testing simpleInvoice02
        String[] outputInvoiceWindowArray00 = outputInvoiceWindowList.get(1).split(" ");
        assertThat(outputInvoiceWindowArray00[1]).isEqualTo(simpleInvoice02.getStoreID());
        assertThat(outputInvoiceWindowArray00[3]).isEqualTo("1");

        // testing simpleInvoice01 and simpleInvoice03
        String[] outputInvoiceWindowArray01 = outputInvoiceWindowList.get(2).split(" ");
        assertThat(outputInvoiceWindowArray01[1]).isEqualTo(simpleInvoice01.getStoreID());
        assertThat(outputInvoiceWindowArray01[3]).isEqualTo("2");

        //No more output in topic
        assertThat(outputTopic.isEmpty()).isTrue();
    }
}
