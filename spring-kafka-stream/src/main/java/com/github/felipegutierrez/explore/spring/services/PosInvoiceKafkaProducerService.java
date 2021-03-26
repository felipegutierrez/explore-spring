package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.model.PosInvoice;
import com.github.felipegutierrez.explore.spring.model.PosInvoiceAvro;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PosInvoiceKafkaProducerService {
    @Value("${application.configs.invoice.topic.json.name}")
    private String TOPIC_JSON_NAME;
    @Value("${application.configs.invoice.topic.avro.name}")
    private String TOPIC_AVRO_NAME;

    @Qualifier("confluent-invoice-json")
    @Autowired
    private KafkaTemplate<String, PosInvoice> kafkaTemplateJson;

    @Qualifier("confluent-invoice-avro")
    @Autowired
    private KafkaTemplate<String, PosInvoiceAvro> kafkaTemplateAvro;

    public void sendMessage(PosInvoice invoice) {
        log.info(String.format("Producing JSON Invoice No: %s Customer Type: %s",
                invoice.getInvoiceNumber(),
                invoice.getCustomerType()));
        kafkaTemplateJson.send(TOPIC_JSON_NAME, invoice.getStoreID(), invoice);
    }

    public void sendMessage(PosInvoiceAvro invoice) {
        log.info(String.format("Producing AVRO Invoice No: %s Customer Type: %s",
                invoice.getInvoiceNumber(),
                invoice.getCustomerType()));
        kafkaTemplateAvro.send(TOPIC_AVRO_NAME, invoice.getStoreID(), invoice);
    }
}
