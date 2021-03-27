package com.github.felipegutierrez.explore.spring;

import com.github.felipegutierrez.explore.spring.services.PosInvoiceKafkaProducerService;
import com.github.felipegutierrez.explore.spring.utils.InvoiceGenerator;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringKafkaStreamApplication implements ApplicationRunner {

    @Autowired
    private PosInvoiceKafkaProducerService posInvoiceKafkaProducerService;
    @Autowired
    private InvoiceGenerator invoiceGenerator;
    @Value("${application.configs.invoice.count}")
    private int INVOICE_COUNT;
    @Value("${application.configs.invoice.topic.json.name}")
    private String INVOICE_JSON_TOPIC_NAME;
    @Value("${application.configs.invoice.topic.avro.name}")
    private String INVOICE_AVRO_TOPIC_NAME;
    @Value("${application.configs.users.topic.name}")
    private String USERS_TOPIC_NAME;
    @Value("${application.configs.loyalty.topic.avro.name}")
    private String LOYALTY_AVRO_TOPIC_NAME;
    @Value("${application.configs.hadoop.topic.avro.name}")
    private String HADOOP_AVRO_TOPIC_NAME;

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaStreamApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        disclaimers();
        if (args.containsOption("json")) {
            for (int i = 0; i < INVOICE_COUNT; i++) {
                posInvoiceKafkaProducerService.sendMessage(invoiceGenerator.getNextInvoiceJson());
                Thread.sleep(1000);
            }
        }
        if (args.containsOption("avro")) {
            for (int i = 0; i < INVOICE_COUNT; i++) {
                posInvoiceKafkaProducerService.sendMessage(invoiceGenerator.getNextInvoiceAvro());
                Thread.sleep(1000);
            }
        }
    }

    @Bean
    NewTopic createInvoiceJsonTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(INVOICE_JSON_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createInvoiceAvroTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(INVOICE_AVRO_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createUserTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(USERS_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createLoyaltyAvroTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(LOYALTY_AVRO_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createHadoopAvroTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(HADOOP_AVRO_TOPIC_NAME, partitions, replicationFactor);
    }

    private void disclaimers() {
        System.out.println("confluent local services start");
        System.out.println("http://localhost:9021/");
        System.out.println("kafka-console-consumer --topic pos-topic --bootstrap-server localhost:9092 --from-beginning");
        System.out.println("kafka-avro-console-consumer --topic pos-avro-topic --bootstrap-server localhost:9092 --property schema.registry.url=http://localhost:8081 --from-beginning");
        System.out.println();
        System.out.println("using parameter --json");
        System.out.println("kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic loyalty-avro-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=\":\"");
        System.out.println("kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic hadoop-sink-avro-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=\":\"");
        System.out.println();
        System.out.println("using parameter --avro");
        System.out.println("");
        System.out.println("");
        System.out.println();
    }
}
