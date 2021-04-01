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

    @Value("${application.configs.users.topic.name}")
    private String USERS_TOPIC_NAME;

    @Value("${application.configs.invoice.count}")
    private int INVOICE_COUNT;
    @Value("${application.configs.invoice.topic.json.name}")
    private String INVOICE_JSON_TOPIC_NAME;
    @Value("${application.configs.invoice.topic.avro.name}")
    private String INVOICE_AVRO_TOPIC_NAME;

    @Value("${application.configs.loyalty.topic.avro.name}")
    private String LOYALTY_AVRO_TOPIC_NAME;
    @Value("${application.configs.loyalty.topic.json.name}")
    private String LOYALTY_JSON_TOPIC_NAME;
    @Value("${application.configs.hadoop.topic.avro.name}")
    private String HADOOP_AVRO_TOPIC_NAME;
    @Value("${application.configs.hadoop.topic.json.name}")
    private String HADOOP_JSON_TOPIC_NAME;
    @Value("${application.configs.loyalty.topic.avro.ex.name}")
    private String LOYALTY_AVRO_EXACTLY_ONCE_TOPIC_NAME;
    @Value("${application.configs.hadoop.topic.avro.ex.name}")
    private String HADOOP_AVRO_EXACTLY_ONCE_TOPIC_NAME;

    @Value("${application.configs.order.xml.input.topic.name}")
    private String ORDER_XML_INPUT_TOPIC_NAME;
    @Value("${application.configs.order.india.output.topic.name}")
    private String ORDER_INDIA_OUTPUT_TOPIC_NAME;
    @Value("${application.configs.order.abroad.output.topic.name}")
    private String ORDER_ABROAD_OUTPUT_TOPIC_NAME;
    @Value("${application.configs.order.error.topic.name}")
    private String ORDER_ERROR_TOPIC_NAME;

    @Value("${application.configs.stock.tick.topic.name}")
    private String STOCK_TICK_TOPIC_NAME;

    @Value("${application.configs.words.streaming.topic.name}")
    private String WORDS_STREAMING_TOPIC_NAME;
    @Value("${application.configs.words.streaming.output.topic.name}")
    private String WORDS_STREAMING_OUTPUT_TOPIC_NAME;

    @Value("${application.configs.employees.input.topic.name}")
    private String EMPLOYEES_INPUT_TOPIC_NAME;

    @Value("${application.configs.simple.invoice.input.topic}")
    private String SIMPLE_INVOICE_INPUT_TOPIC_NAME;

    @Value("${application.configs.user.click.input.topic}")
    private String USER_CLICK_INPUT_TOPIC_NAME;

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
        if (args.containsOption("xml")) {

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
    NewTopic createLoyaltyAvroExactlyOnceTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(LOYALTY_AVRO_EXACTLY_ONCE_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createLoyaltyJsonTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(LOYALTY_JSON_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createHadoopAvroTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(HADOOP_AVRO_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createHadoopAvroExactlyOnceTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(HADOOP_AVRO_EXACTLY_ONCE_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createHadoopJsonTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(HADOOP_JSON_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createOrderXmlInputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(ORDER_XML_INPUT_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createOrderIndiaOutputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(ORDER_INDIA_OUTPUT_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createOrderAbroadOutputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(ORDER_ABROAD_OUTPUT_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createOrderErrorTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(ORDER_ERROR_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createStockTickTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(STOCK_TICK_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createWordsStreamingTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(WORDS_STREAMING_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createWordsStreamingOutputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(WORDS_STREAMING_OUTPUT_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createEmployeesInputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(EMPLOYEES_INPUT_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createSimpleInvoiceInputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(SIMPLE_INVOICE_INPUT_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createUserClickInputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(USER_CLICK_INPUT_TOPIC_NAME, partitions, replicationFactor);
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
