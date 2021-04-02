package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.bindings.OTPListenerBinding;
import com.github.felipegutierrez.explore.spring.model.PaymentConfirmation;
import com.github.felipegutierrez.explore.spring.model.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;

@Slf4j
@Service
@EnableBinding(OTPListenerBinding.class)
public class OTPValidationService {

    @Autowired
    private RecordBuilder recordBuilder;

    @StreamListener
    public void process(@Input("payment-request-channel") KStream<String, PaymentRequest> request,
                        @Input("payment-confirmation-channel") KStream<String, PaymentConfirmation> confirmation) {
        request.foreach((k, v) -> log.info("Request Key = " + k + " Created Time = "
                + Instant.ofEpochMilli(v.getCreatedTime()).atOffset(ZoneOffset.UTC)));

        confirmation.foreach((k, v) -> log.info("Confirmation Key = " + k + " Created Time = "
                + Instant.ofEpochMilli(v.getCreatedTime()).atOffset(ZoneOffset.UTC)));


    }
}
