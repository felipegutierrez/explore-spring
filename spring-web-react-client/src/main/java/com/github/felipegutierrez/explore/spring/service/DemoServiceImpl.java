package com.github.felipegutierrez.explore.spring.service;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DemoServiceImpl {

    public Mono<String> sendRequestFor() {
        String value = "this is a request to send";
        return Mono.just(value);
    }
}
