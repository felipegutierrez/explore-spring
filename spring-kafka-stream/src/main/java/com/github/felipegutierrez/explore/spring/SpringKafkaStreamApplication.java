package com.github.felipegutierrez.explore.spring;

import com.github.felipegutierrez.explore.spring.config.KafkaTopicsConfig;
import com.github.felipegutierrez.explore.spring.services.PosInvoiceKafkaProducerService;
import com.github.felipegutierrez.explore.spring.utils.InvoiceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringKafkaStreamApplication implements ApplicationRunner {

    @Autowired
    private PosInvoiceKafkaProducerService posInvoiceKafkaProducerService;
    @Autowired
    private InvoiceGenerator invoiceGenerator;
    @Autowired
    private KafkaTopicsConfig kafkaTopicsConfig;

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaStreamApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        disclaimers();
        if (args.containsOption("json")) {
            for (int i = 0; i < kafkaTopicsConfig.getINVOICE_COUNT(); i++) {
                posInvoiceKafkaProducerService.sendMessage(invoiceGenerator.getNextInvoiceJson());
                Thread.sleep(1000);
            }
        }
        if (args.containsOption("avro")) {
            for (int i = 0; i < kafkaTopicsConfig.getINVOICE_COUNT(); i++) {
                posInvoiceKafkaProducerService.sendMessage(invoiceGenerator.getNextInvoiceAvro());
                Thread.sleep(1000);
            }
        }
        if (args.containsOption("xml")) {

        }
    }

    private void disclaimers() {
        System.out.println("confluent local services start");
        System.out.println("http://localhost:9021/");
        System.out.println("kafka-console-consumer --topic pos-topic --bootstrap-server localhost:9092 --from-beginning");
        System.out.println("kafka-avro-console-consumer --topic pos-avro-topic --bootstrap-server localhost:9092 --property schema.registry.url=http://localhost:8081 --from-beginning");
        System.out.println();
        System.out.println("using parameter --json to consume messages serialized in JSON to AVRO");
        System.out.println("kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic loyalty-avro-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=\":\"");
        System.out.println("kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic hadoop-sink-avro-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=\":\"");
        System.out.println();
        System.out.println("using parameter --avro to consume messages serialized in AVRO to JSON");
        System.out.println("kafka-json-schema-console-consumer --bootstrap-server localhost:9092 --topic loyalty-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=\":\"");
        System.out.println("kafka-json-schema-console-consumer --bootstrap-server localhost:9092 --topic hadoop-sink-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=\":\"");
        System.out.println();
    }
}
