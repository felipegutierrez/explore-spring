package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.model.Order;
import com.github.felipegutierrez.explore.spring.model.OrderEnvelop;
import com.github.felipegutierrez.explore.spring.utils.CustomSerdes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.function.Function;

import static com.github.felipegutierrez.explore.spring.utils.OrderConstants.*;

@Slf4j
@Configuration
@EnableAutoConfiguration
public class OrderListenerFunctionalService {

    @Value("${application.configs.order.error.topic.name}")
    private String ERROR_TOPIC;

    @Bean
    public Function<KStream<String, String>, KStream<String, Order>[]> streamOrderBranches() {

        Predicate<String, Order> isIndiaOrder = (k, v) -> v.getShipTo().getCountry().equalsIgnoreCase("india");
        Predicate<String, Order> isAbroadOrder = (k, v) -> !v.getShipTo().getCountry().equalsIgnoreCase("india");

        return input -> {
            KStream<String, OrderEnvelop> orderEnvelopKStream = input
                    .peek((key, value) -> log.info("key: {}, Value: {}", key, value))
                    .map((key, value) -> {
                        OrderEnvelop orderEnvelop = new OrderEnvelop();
                        orderEnvelop.setXmlOrderKey(key);
                        orderEnvelop.setXmlOrderValue(value);
                        try {
                            JAXBContext jaxbContext = JAXBContext.newInstance(Order.class);
                            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

                            orderEnvelop.setValidOrder((Order) jaxbUnmarshaller.unmarshal(new StringReader(value)));
                            orderEnvelop.setOrderTag(VALID_ORDER);

                            if (orderEnvelop.getValidOrder().getShipTo().getCity().isEmpty()) {
                                log.error("Missing destination City");
                                orderEnvelop.setOrderTag(ADDRESS_ERROR);
                            }

                        } catch (JAXBException e) {
                            log.error("Failed to Unmarshal the incoming XML");
                            orderEnvelop.setOrderTag(PARSE_ERROR);
                        }
                        return KeyValue.pair(orderEnvelop.getOrderTag(), orderEnvelop);
                    });

            orderEnvelopKStream
                    .filter((k, v) -> !k.equalsIgnoreCase(VALID_ORDER))
                    .to(ERROR_TOPIC, Produced.with(CustomSerdes.String(), CustomSerdes.OrderEnvelop()));

            KStream<String, Order>[] validOrders = orderEnvelopKStream
                    .filter((k, v) -> k.equalsIgnoreCase(VALID_ORDER))
                    .map((k, v) -> KeyValue.pair(v.getValidOrder().getOrderId(), v.getValidOrder()))
                    .peek((k, v) -> log.info(String.format("Valid Order with ID: %s", v.getOrderId())))
                    .branch(isIndiaOrder, isAbroadOrder);
            return validOrders;
        };
    }
}
