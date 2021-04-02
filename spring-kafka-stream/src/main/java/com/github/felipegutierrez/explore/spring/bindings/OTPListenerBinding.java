package com.github.felipegutierrez.explore.spring.bindings;

import com.github.felipegutierrez.explore.spring.model.PaymentConfirmation;
import com.github.felipegutierrez.explore.spring.model.PaymentRequest;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface OTPListenerBinding {
    @Input("payment-request-channel")
    KStream<String, PaymentRequest> requestInputStream();

    @Input("payment-confirmation-channel")
    KStream<String, PaymentConfirmation> confirmationInputStream();
}
