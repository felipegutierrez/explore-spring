package com.github.felipegutierrez.explore.spring.extractors;

import com.github.felipegutierrez.explore.spring.model.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class PaymentRequestTimeExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        PaymentRequest request = (PaymentRequest) record.value();
        return ((request.getCreatedTime() > 0) ? request.getCreatedTime() : partitionTime);
    }

    @Bean
    public TimestampExtractor requestTimeExtractor() {
        return new PaymentRequestTimeExtractor();
    }
}
