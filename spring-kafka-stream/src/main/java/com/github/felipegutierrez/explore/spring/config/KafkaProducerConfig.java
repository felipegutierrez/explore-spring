package com.github.felipegutierrez.explore.spring.config;

import com.github.felipegutierrez.explore.spring.model.PosInvoice;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, PosInvoice> confluentProducerFactoryInvoiceJsonSerializer() {

        HashMap<String, Object> configProps = new HashMap<String, Object>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
        configProps.put("spring.json.add.type.headers", "false");
        return new DefaultKafkaProducerFactory<String, PosInvoice>(configProps);
    }

    @Bean(name = "confluent-invoice-json")
    public KafkaTemplate<String, PosInvoice> confluentProducerInvoiceJsonTemplate() {
        return new KafkaTemplate<String, PosInvoice>(confluentProducerFactoryInvoiceJsonSerializer());
    }
}
