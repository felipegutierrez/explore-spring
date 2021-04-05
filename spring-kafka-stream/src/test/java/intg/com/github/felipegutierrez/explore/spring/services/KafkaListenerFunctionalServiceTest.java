package com.github.felipegutierrez.explore.spring.services;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getRecords;
import static org.springframework.kafka.test.utils.KafkaTestUtils.producerProps;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {"server.port=0"})
@ExtendWith(SpringExtension.class)
@DirtiesContext
@EmbeddedKafka(topics = {"output-func-topic"}, partitions = 1)
@TestPropertySource(properties = {
        "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"
})
public class KafkaListenerFunctionalServiceTest {

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @SpyBean
    KafkaListenerFunctionalService kafkaListenerFunctionalServiceSpy;

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
            template.setDefaultTopic("input-func-topic");

            template.sendDefault("hello1").get();
            // verify(kafkaListenerFunctionalServiceSpy).transformToUpperCase();

            template.sendDefault("hello2").get();
            // verify(kafkaListenerFunctionalServiceSpy, times(1)).transformToUpperCase();

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
}
