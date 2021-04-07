package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.config.KafkaTopicsConfig;
import com.github.felipegutierrez.explore.spring.model.Order;
import com.github.felipegutierrez.explore.spring.model.OrderEnvelop;
import org.apache.kafka.clients.producer.ProducerConfig;
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
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.Properties;
import java.util.function.Function;

import static com.github.felipegutierrez.explore.spring.utils.OrderConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {"server.port=0"})
@ExtendWith(SpringExtension.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1)
public class OrderListenerFunctionalServiceTest {

    final Serde<String> stringSerde = Serdes.String();
    final JsonSerde<Order> orderSerde = new JsonSerde<>(Order.class);
    @Autowired
    KafkaTopicsConfig kafkaTopicsConfig;
    @Autowired
    OrderListenerFunctionalService orderListenerFunctionalService;
    private String INPUT_TOPIC;
    private String OUTPUT_TOPIC_INDIA;
    private String OUTPUT_TOPIC_ABROAD;
    private TopologyTestDriver testDriver;
    private TestInputTopic inputTopic;
    private TestOutputTopic outputTopicIndia;
    private TestOutputTopic outputTopicAbroad;

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
        INPUT_TOPIC = kafkaTopicsConfig.getORDER_FUNC_INPUT_TOPIC_NAME();
        OUTPUT_TOPIC_INDIA = kafkaTopicsConfig.getORDER_INDIA_FUNC_OUTPUT_TOPIC_NAME();
        OUTPUT_TOPIC_ABROAD = kafkaTopicsConfig.getORDER_ABROAD_FUNC_OUTPUT_TOPIC_NAME();

        final StreamsBuilder builder = new StreamsBuilder();
        buildStreamProcessingPipeline(builder);

        testDriver = new TopologyTestDriver(builder.build(), getStreamsConfiguration());
        inputTopic = testDriver.createInputTopic(INPUT_TOPIC, stringSerde.serializer(), stringSerde.serializer());
        outputTopicIndia = testDriver.createOutputTopic(OUTPUT_TOPIC_INDIA, stringSerde.deserializer(), orderSerde.deserializer());
        outputTopicAbroad = testDriver.createOutputTopic(OUTPUT_TOPIC_ABROAD, stringSerde.deserializer(), orderSerde.deserializer());
    }

    private void buildStreamProcessingPipeline(StreamsBuilder builder) {
        KStream<String, String> input = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));
        final Function<KStream<String, String>, KStream<String, Order>[]> process = orderListenerFunctionalService.streamOrderBranches();
        final KStream<String, Order>[] output = process.apply(input);
        output[0].to(OUTPUT_TOPIC_INDIA, Produced.with(Serdes.String(), new JsonSerde<>(Order.class)));
        output[1].to(OUTPUT_TOPIC_ABROAD, Produced.with(Serdes.String(), new JsonSerde<>(Order.class)));
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

    @Test
    public void testOrderToIndia() {
        // Indian Order --------------
        // <?xml version="1.0" encoding="UTF-8"?><order order-id="889925" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="order.xsd"><order-by>Abdul Hamid</order-by><ship-to><name>Nawab Aalam</name><address>42 Park Squire</address><city>Bangalore</city><country>India</country></ship-to><item><title>Empire Burlesque</title><note>Special Edition</note><quantity>1</quantity><price>10.90</price></item><item><title>Hide your heart</title><quantity>1</quantity><price>9.90</price></item></order>
        String indiaOrderXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><order order-id=\"889925\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"order.xsd\"><order-by>Abdul Hamid</order-by><ship-to><name>Nawab Aalam</name><address>42 Park Squire</address><city>Bangalore</city><country>India</country></ship-to><item><title>Empire Burlesque</title><note>Special Edition</note><quantity>1</quantity><price>10.90</price></item><item><title>Hide your heart</title><quantity>1</quantity><price>9.90</price></item></order>";
        Order indiaOrderExpected = createOrder(null, indiaOrderXml);
        inputTopic.pipeInput(indiaOrderXml);

        //Read and validate output
        final Order outputOrder = (Order) outputTopicIndia.readValue();
        assertThat(outputOrder).isNotNull();

        System.out.println("outputOrder");
        System.out.println(outputOrder.getOrderId());
        System.out.println(outputOrder.getOrderBy());
        System.out.println(outputOrder.getShipTo().getName());
        System.out.println(outputOrder.getShipTo().getAddress());
        System.out.println(outputOrder.getShipTo().getCity());
        System.out.println(outputOrder.getShipTo().getCountry());
        System.out.println(outputOrder.getItem().get(0).getTitle());
        System.out.println(outputOrder.getItem().get(0).getNote());
        System.out.println(outputOrder.getItem().get(0).getQuantity());
        System.out.println(outputOrder.getItem().get(0).getPrice());
        assertThat(outputOrder).usingRecursiveComparison().isEqualTo(indiaOrderExpected);

        //No more output in topic
        assertThat(outputTopicIndia.isEmpty()).isTrue();
    }

    private Order createOrder(String key, String value) {
        OrderEnvelop orderEnvelop = new OrderEnvelop();
        orderEnvelop.setXmlOrderKey(key);
        orderEnvelop.setXmlOrderValue(value);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Order.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            orderEnvelop.setValidOrder((Order) jaxbUnmarshaller.unmarshal(new StringReader(value)));
            orderEnvelop.setOrderTag(VALID_ORDER);

            if (orderEnvelop.getValidOrder().getShipTo().getCity().isEmpty()) {
                System.err.println("Missing destination City");
                orderEnvelop.setOrderTag(ADDRESS_ERROR);
            }

        } catch (JAXBException e) {
            System.err.println("Failed to Unmarshal the incoming XML");
            orderEnvelop.setOrderTag(PARSE_ERROR);
        }
        return orderEnvelop.getValidOrder();
    }
}
