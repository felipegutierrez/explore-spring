package com.github.felipegutierrez.explore.spring.services;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.kstream.KStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getRecords;
import static org.springframework.kafka.test.utils.KafkaTestUtils.producerProps;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {"server.port=0"})
@ExtendWith(SpringExtension.class)
@DirtiesContext
@EmbeddedKafka(topics = {"words-out-topic"}, partitions = 1)
@TestPropertySource(properties = {
        "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"
})
public class WordListenerServiceTest {

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @SpyBean
    WordListenerService wordListenerServiceSpy;

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
        Map<String, String> actualResultMap = new HashMap<>();
        Map<String, String> expectedResultMap = new HashMap<>();
        expectedResultMap.put("hello", "3");
        expectedResultMap.put("felipe", "2");
        expectedResultMap.put("kafka", "1");
        expectedResultMap.put("stream", "1");

        Map<String, Object> senderProps = producerProps(embeddedKafkaBroker);
        DefaultKafkaProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<>(senderProps);
        try {
            KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf, true);
            template.setDefaultTopic("words-in-topic");

            template.sendDefault("hello hello hello felipe felipe kafka stream").get();
            verify(wordListenerServiceSpy, times(1)).process(isA(KStream.class));

            /*
            ConsumerRecords<String, String> cr = getRecords(consumer);
            cr.iterator().forEachRemaining(r -> {
                System.out.println("result word count key: " + r.key() + " value:" + r.value());
                actualResultMap.put(r.key(), r.value());
            });
             */
            /*
            int receivedAll = 0;
            while (receivedAll < 4) {
                ConsumerRecords<String, String> cr = getRecords(consumer);
                receivedAll = receivedAll + cr.count();
                cr.iterator().forEachRemaining(r -> {
                    System.out.println("result word count key: " + r.key() + " value:" + r.value());
                    actualResultMap.put(r.key(), r.value());
                });
            }
            // Assertions.assertThat(actualResultMap.equals(expectedResultMap)).isTrue();
             */

        } finally {
            pf.destroy();
        }
    }
}
