package com.github.felipegutierrez.explore.spring.extractors;

import com.github.felipegutierrez.explore.spring.model.SimpleInvoice;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * this is a time extractor for the SimpleInvoice which arrives on the topic "simple-invoice-topic".
 */
@Configuration
public class InvoiceTimeExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> consumerRecord, long partitionTime) {
        SimpleInvoice invoice = (SimpleInvoice) consumerRecord.value();
        return ((invoice.getCreatedTime() > 0) ? invoice.getCreatedTime() : partitionTime);
    }

    /**
     * The spring framework will listen to this time extractor at the application.yaml file and use it
     * to extract the timestamp for each event.
     * invoice-input-channel.consumer.timestampExtractorBeanName: invoiceTimesExtractor
     *
     * @return
     */
    @Bean
    public TimestampExtractor invoiceTimesExtractor() {
        return new InvoiceTimeExtractor();
    }
}
