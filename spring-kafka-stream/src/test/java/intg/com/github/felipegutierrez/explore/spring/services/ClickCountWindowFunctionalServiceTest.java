package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.config.KafkaTopicsConfig;
import com.github.felipegutierrez.explore.spring.model.UserClick;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {"server.port=0"})
@ExtendWith(SpringExtension.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1)
public class ClickCountWindowFunctionalServiceTest {

    final Serde<String> stringSerde = Serdes.String();
    final JsonSerde<UserClick> userClickSerde = new JsonSerde<>(UserClick.class);
    @Autowired
    KafkaTopicsConfig kafkaTopicsConfig;
    @Autowired
    ClickCountWindowFunctionalService clickCountWindowFunctionalService;
    private String INPUT_TOPIC;
    private String OUTPUT_TOPIC;
    private TopologyTestDriver testDriver;
    private TestInputTopic inputTopic;
    private TestOutputTopic outputTopic;

    static Properties getStreamsConfiguration() {
        final Properties streamsConfiguration = new Properties();
        // Need to be set even these do not matter with TopologyTestDriver
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "TopologyTestDriver");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        streamsConfiguration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, String.class);
        streamsConfiguration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return streamsConfiguration;
    }

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
        // Feed word "Hello" to inputTopic and no kafka key, timestamp is irrelevant in this case
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

    @Test
    public void testMultipleClicks() {
        final List<UserClick> inputLines = Arrays.asList(
                new UserClick("USR101", 1549360860000l, "Kafka", "Home"),
                new UserClick("USR102", 1549360920000l, "Books", "Home"),
                new UserClick("USR101", 1549361040000l, "Books", "Kafka"),
                new UserClick("USR102", 1549361400000l, "Home", "Buy")
        );
        final List<KeyValue<String, UserClick>> inputRecords = inputLines
                .stream()
                .map(v -> new KeyValue<String, UserClick>(v.getUserID(), v))
                .collect(Collectors.toList());

        final Map<String, Long> expectedWordCounts = new HashMap<>();
        expectedWordCounts.put("USR101", 2L);
        expectedWordCounts.put("USR102", 2L);

        inputTopic.pipeKeyValueList(inputRecords, Instant.ofEpochSecond(1L), Duration.ofMillis(1000L));
        final Map<String, Long> actualWordCounts = getOutputList();
        assertThat(actualWordCounts).containsAllEntriesOf(expectedWordCounts).hasSameSizeAs(expectedWordCounts);
    }

    private Map<String, Long> getOutputList() {
        final Map<String, Long> output = new HashMap<>();
        String outputRow;
        while (!outputTopic.isEmpty()) {
            outputRow = (String) outputTopic.readValue();
            String[] outputRows = outputRow.split(" ");
            if (outputRows.length == 4) {
                if (!Strings.isNullOrEmpty(outputRows[1])
                        && !Strings.isNullOrEmpty(outputRows[3])
                        && !"null".equalsIgnoreCase(outputRows[3])) {
                    output.put(outputRows[1], Long.valueOf(outputRows[3]));
                }
            }
        }
        return output;
    }
}
