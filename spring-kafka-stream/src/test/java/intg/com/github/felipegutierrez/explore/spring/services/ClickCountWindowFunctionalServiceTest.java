package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.config.KafkaTopicsConfig;
import com.github.felipegutierrez.explore.spring.model.AdClicksByNewsType;
import com.github.felipegutierrez.explore.spring.model.UserClick;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.apache.kafka.streams.StreamsBuilder;

import java.util.*;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getRecords;
import static org.springframework.kafka.test.utils.KafkaTestUtils.producerProps;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {"server.port=0"})
@ExtendWith(SpringExtension.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1)
//@EmbeddedKafka(topics = {"user-clicks-func-out-topic"}, partitions = 1)
//@TestPropertySource(properties = {
//        "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
//        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"
//})
public class ClickCountWindowFunctionalServiceTest {

    @Autowired
    KafkaTopicsConfig kafkaTopicsConfig;

    private String INPUT_TOPIC;
    private String OUTPUT_TOPIC;
    private TopologyTestDriver testDriver;
    private TestInputTopic inputTopic;
    private TestOutputTopic outputTopic;

    final Serde<String> stringSerde = Serdes.String();
    final JsonSerde<UserClick> userClickSerde = new JsonSerde<>(UserClick.class);

    @Autowired
    ClickCountWindowFunctionalService clickCountWindowFunctionalService;

    @BeforeEach
    public void setUp() {
        INPUT_TOPIC = kafkaTopicsConfig.getUSER_CLICK_FUNC_INPUT_TOPIC_NAME();
        OUTPUT_TOPIC = kafkaTopicsConfig.getUSER_CLICK_FUNC_OUTPUT_TOPIC_NAME();

        final StreamsBuilder builder = new StreamsBuilder();
        buildStreamProcessingPipeline(builder);

        testDriver = new TopologyTestDriver(builder.build(), getStreamsConfiguration());
        inputTopic = testDriver.createInputTopic(INPUT_TOPIC, stringSerde.serializer(), userClickSerde.serializer());
        outputTopic = testDriver.createOutputTopic(OUTPUT_TOPIC, stringSerde.deserializer(), stringSerde.deserializer());
    }

    static Properties getStreamsConfiguration() {
        final Properties streamsConfiguration = new Properties();
        // Need to be set even these do not matter with TopologyTestDriver
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "TopologyTestDriver");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        streamsConfiguration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, String.class);
        streamsConfiguration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return streamsConfiguration;
    }

    private void buildStreamProcessingPipeline(StreamsBuilder builder) {
        KStream<String, UserClick> input = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), new JsonSerde<>(UserClick.class)));
        final Function<KStream<String, UserClick>, KStream<String, String>> process = clickCountWindowFunctionalService.clickUsersFunctionalCountWindow();
        final KStream<String, String> output = process.apply(input);
        output.to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));
    }

    @AfterEach
    public void tearDown() {
        try {
            testDriver.close();
        } catch (final RuntimeException e) {
            // https://issues.apache.org/jira/browse/KAFKA-6647 causes exception when executed in Windows, ignoring it
            // Logged stacktrace cannot be avoided
            System.out.println("Ignoring exception, test failing in Windows due this exception:" + e.getLocalizedMessage());
        }
    }

    /**
     * Simple test validating count of one Click
     */
    @Test
    public void testOneClick() {
        final String nullKey = null;
        //Feed word "Hello" to inputTopic and no kafka key, timestamp is irrelevant in this case
        // inputTopic.pipeInput("USR101", "{\"UserID\": \"USR101\",\"CreatedTime\": 1549360860000,\"CurrentLink\": \"NULL\", \"NextLink\": \"Home\"}");
        UserClick userClick01 = new UserClick("USR101", 1549360860000l, "NULL", "Home");
        inputTopic.pipeInput("USR101", userClick01);

        //Read and validate output
        final Object output = outputTopic.readValue();
        assertThat(output).isNotNull();
        // key: USR101, value: UserID: USR101 Window start: 2021-04-06T15:36:36.068Z Window end: 2021-04-06T15:36:36.068Z Count: 1 Window#: 1336418273
        assertThat(output).isEqualTo("UserID: USR101 Count: 1");

        //No more output in topic
        assertThat(outputTopic.isEmpty()).isTrue();
    }

    /*
    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @SpyBean
    ClickCountWindowFunctionalService clickCountWindowFunctionalServiceSpy;

    private Consumer<String, String> consumer;

    @BeforeEach
    public void setUp() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker));
        consumer = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer()).createConsumer();
        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
    }

    @AfterEach
    public void tearDown() {
        consumer.close();
    }

    @Test
    public void SimpleProcessorApplicationTest() throws ExecutionException, InterruptedException {
        Set<String> actualResultSet = new HashSet<>();
        Set<String> expectedResultSet = new HashSet<>();
        expectedResultSet.add("HELLO1");
        expectedResultSet.add("HELLO2");

        Map<String, Object> senderProps = producerProps(embeddedKafkaBroker);
        DefaultKafkaProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<>(senderProps);
        try {
            KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf, true);
            template.setDefaultTopic("user-clicks-func-in-topic");

            template.sendDefault("USR101:{\"UserID\": \"USR101\",\"CreatedTime\": \"1549360860000\",\"CurrentLink\": \"NULL\", \"NextLink\": \"Home\"}").get();
            // verify(clickCountWindowFunctionalServiceSpy).transformToUpperCase();

            template.sendDefault("hello2").get();
            // verify(clickCountWindowFunctionalServiceSpy, times(1)).transformToUpperCase();

            int receivedAll = 0;
            while (receivedAll < 2) {
                ConsumerRecords<String, String> cr = getRecords(consumer);
                receivedAll = receivedAll + cr.count();
                cr.iterator().forEachRemaining(r -> {
                    System.out.println("result functional: " + r.value());
                    actualResultSet.add(r.value());
                });
            }

            assertThat(actualResultSet.equals(expectedResultSet)).isTrue();
        } finally {
            pf.destroy();
        }
    }
     */
}
