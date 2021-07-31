package com.github.felipegutierrez.explore.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BookService {

    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    private final WebClient webClient;

    // @Autowired
    private ReactiveCircuitBreaker readingListCircuitBreaker;

    // public BookService(ReactiveCircuitBreakerFactory circuitBreakerFactory) {
    public BookService() {
        this.webClient = WebClient.builder().baseUrl("http://localhost:8090").build();
        // this.readingListCircuitBreaker = circuitBreakerFactory.create("recommended");
        this.readingListCircuitBreaker = factory().create("recommended");
    }

    public Mono<String> readingList() {
        return readingListCircuitBreaker.run(webClient.get().uri("/recommended").retrieve().bodyToMono(String.class), throwable -> {
            LOG.warn("Error making request to book service", throwable);
            return Mono.just("Cloud Native Java (O'Reilly)");
        });
    }

    @Bean
    public ReactiveResilience4JCircuitBreakerFactory factory() {
        return new ReactiveResilience4JCircuitBreakerFactory();
    }
}
