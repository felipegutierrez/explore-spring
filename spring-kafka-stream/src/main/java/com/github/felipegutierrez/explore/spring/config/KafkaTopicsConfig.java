package com.github.felipegutierrez.explore.spring.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@lombok.Getter
@Configuration
public class KafkaTopicsConfig {

    @Value("${application.configs.process.input.topic.name}")
    private String PROCESS_INPUT_TOPIC_NAME;
    @Value("${application.configs.process.output.topic.name}")
    private String PROCESS_OUTPUT_TOPIC_NAME;

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

    @Value("${application.configs.payment.request.input.topic}")
    private String PAYMENT_REQUEST_INPUT_TOPIC_NAME;
    @Value("${application.configs.payment.confirmation.input.topic}")
    private String PAYMENT_CONFIRMATION_INPUT_TOPIC_NAME;

    @Value("${application.configs.user.master.input.topic}")
    private String USER_MASTER_INPUT_TOPIC_NAME;
    @Value("${application.configs.user.login.input.topic}")
    private String USER_LOGIN_INPUT_TOPIC_NAME;

    @Value("${application.configs.ad.inventories.input.topic}")
    private String AD_INVENTORIES_INPUT_TOPIC_NAME;
    @Value("${application.configs.ad.clicks.input.topic}")
    private String AD_CLICKS_INPUT_TOPIC_NAME;

    @Value("${application.configs.process.func.input.topic.name}")
    private String PROCESS_FUNC_INPUT_TOPIC_NAME;
    @Value("${application.configs.process.func.output.topic.name}")
    private String PROCESS_FUNC_OUTPUT_TOPIC_NAME;

    @Bean
    NewTopic createProcessInputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(PROCESS_INPUT_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createProcessOutputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(PROCESS_OUTPUT_TOPIC_NAME, partitions, replicationFactor);
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

    @Bean
    NewTopic createPaymentRequestInputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(PAYMENT_REQUEST_INPUT_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createPaymentConfirmationInputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(PAYMENT_CONFIRMATION_INPUT_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createUserMasterInputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(USER_MASTER_INPUT_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createUserLoginInputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(USER_LOGIN_INPUT_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createAdInventoriesInputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(AD_INVENTORIES_INPUT_TOPIC_NAME, partitions, replicationFactor);
    }
    @Bean
    NewTopic createAdClicksInputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(AD_CLICKS_INPUT_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createProcessFunctionalInputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(PROCESS_FUNC_INPUT_TOPIC_NAME, partitions, replicationFactor);
    }
    @Bean
    NewTopic createProcessFunctionalOutputTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(PROCESS_FUNC_OUTPUT_TOPIC_NAME, partitions, replicationFactor);
    }
}
