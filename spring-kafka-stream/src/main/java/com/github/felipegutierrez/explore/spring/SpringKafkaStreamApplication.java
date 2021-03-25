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
    @Value("${application.configs.invoice.topic.name}")
    private String INVOICE_TOPIC_NAME;
    @Value("${application.configs.users.topic.name}")
    private String USERS_TOPIC_NAME;

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaStreamApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        disclaimers();
        for (int i = 0; i < INVOICE_COUNT; i++) {
            posInvoiceKafkaProducerService.sendMessage(invoiceGenerator.getNextInvoice());
            Thread.sleep(1000);
        }
    }

    @Bean
    NewTopic createInvoiceTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(INVOICE_TOPIC_NAME, partitions, replicationFactor);
    }

    @Bean
    NewTopic createUserTopic() {
        Integer partitions = 1;
        short replicationFactor = 1;
        return new NewTopic(USERS_TOPIC_NAME, partitions, replicationFactor);
    }

    private void disclaimers() {
        System.out.println("confluent local services start");
        System.out.println("http://localhost:9021/");
    }
}
