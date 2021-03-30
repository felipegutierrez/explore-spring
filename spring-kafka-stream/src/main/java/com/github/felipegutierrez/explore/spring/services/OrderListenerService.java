package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.OrderListenerBinding;
import com.github.felipegutierrez.explore.spring.model.Order;
import com.github.felipegutierrez.explore.spring.model.OrderEnvelop;
import com.github.felipegutierrez.explore.spring.utils.CustomSerdes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

import static com.github.felipegutierrez.explore.spring.utils.OrderConstants.*;

@Service
@Slf4j
@EnableBinding(OrderListenerBinding.class)
public class OrderListenerService {

    @Value("${application.configs.order.error.topic.name}")
    private String ERROR_TOPIC;

    @StreamListener("xml-input-channel")
    @SendTo({"india-orders-channel", "abroad-orders-channel"})
    public KStream<String, Order>[] process(KStream<String, String> input) {
        input.foreach((k, v) -> log.info(String.format("Received XML Order Key: %s, Value: %s", k, v)));

        KStream<String, OrderEnvelop> orderEnvelopKStream = input.map((key, value) -> {
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

        KStream<String, Order> validOrders = orderEnvelopKStream
                .filter((k, v) -> k.equalsIgnoreCase(VALID_ORDER))
                .map((k, v) -> KeyValue.pair(v.getValidOrder().getOrderId(), v.getValidOrder()));

        validOrders.foreach((k, v) -> log.info(String.format("Valid Order with ID: %s", v.getOrderId())));

        Predicate<String, Order> isIndiaOrder = (k, v) -> v.getShipTo().getCountry().equalsIgnoreCase("india");
        Predicate<String, Order> isAbroadOrder = (k, v) -> !v.getShipTo().getCountry().equalsIgnoreCase("india");

        return validOrders.branch(isIndiaOrder, isAbroadOrder);
    }
}
