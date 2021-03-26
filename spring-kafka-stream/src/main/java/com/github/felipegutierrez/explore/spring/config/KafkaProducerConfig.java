package com.github.felipegutierrez.explore.spring.config;

import com.github.felipegutierrez.explore.spring.model.PosInvoice;
import com.github.felipegutierrez.explore.spring.model.PosInvoiceAvro;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

@Configuration
public class KafkaProducerConfig {

    @Bean(name = "confluent-invoice-json")
    public KafkaTemplate<String, PosInvoice> confluentProducerInvoiceJsonTemplate() {
        return new KafkaTemplate<String, PosInvoice>(confluentProducerFactoryInvoiceJsonSerializer());
    }

    @Bean(name = "confluent-invoice-avro")
    public KafkaTemplate<String, PosInvoiceAvro> confluentProducerInvoiceAvroTemplate() {
        return new KafkaTemplate<String, PosInvoiceAvro>(confluentProducerFactoryInvoiceAvroSerializer());
    }

    @Bean
    public ProducerFactory<String, PosInvoice> confluentProducerFactoryInvoiceJsonSerializer() {
        HashMap<String, Object> configProps = new HashMap<String, Object>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
        configProps.put("spring.json.add.type.headers", "false");
        return new DefaultKafkaProducerFactory<String, PosInvoice>(configProps);
    }

    @Bean
    public ProducerFactory<String, PosInvoiceAvro> confluentProducerFactoryInvoiceAvroSerializer() {
        HashMap<String, Object> configProps = new HashMap<String, Object>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        configProps.put("schema.registry.url", "http://localhost:8081");
        return new DefaultKafkaProducerFactory<String, PosInvoiceAvro>(configProps);
    }

}
