package com.github.felipegutierrez.explore.spring.extractors;

import com.github.felipegutierrez.explore.spring.model.PaymentConfirmation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class PaymentConfirmationTimeExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        PaymentConfirmation confirmation = (PaymentConfirmation) record.value();
        return ((confirmation.getCreatedTime() > 0) ? confirmation.getCreatedTime() : partitionTime);
    }

    @Bean
    public TimestampExtractor confirmationTimeExtractor() {
        return new PaymentConfirmationTimeExtractor();
    }
}
