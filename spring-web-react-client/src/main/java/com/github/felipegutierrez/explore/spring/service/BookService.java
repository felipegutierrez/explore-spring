package com.github.felipegutierrez.explore.spring.service;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * References:
 *
 * https://mromeh.com/2019/12/26/spring-cloud-gateway-with-resilience4j-circuit-breaker/
 * https://github.com/Romeh/spring-cloud-gateway-resilience4j
 */
@Service
public class BookService {

    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

//    private final WebClient webClient;
//    private final ReactiveCircuitBreaker readingListCircuitBreaker = factory().create("recommended");
    private ReactiveCircuitBreakerFactory cbFactory;
    private WebClient webClient = WebClient.create("http://localhost:8090");

    // public BookService(ReactiveCircuitBreakerFactory circuitBreakerFactory) {
//    public BookService() {
//        this.webClient = WebClient.builder().baseUrl("http://localhost:8090").build();
//        // this.readingListCircuitBreaker = circuitBreakerFactory.create("recommended");
//        // this.readingListCircuitBreaker = factory().create("recommended");
//    }
    public BookService(ReactiveCircuitBreakerFactory cbFactory) {
        // WebClient webClient,
        // this.webClient = webClient;
        this.cbFactory = cbFactory;
    }

    public Mono<String> readingList() {
//        return readingListCircuitBreaker.run(
//                webClient.get()
//                        .uri("/recommended")
//                        .retrieve()
//                        .bodyToMono(String.class), throwable -> {
//                    LOG.warn("Error making request to book service", throwable);
//                    return Mono.just("Cloud Native Java (O'Reilly)");
//                }
//        );
        return webClient.get().uri("/slow").retrieve()
                .bodyToMono(String.class).transform(it -> {
                    ReactiveCircuitBreaker cb = cbFactory.create("slow");
                    return cb.run(it, throwable ->
                            Mono.just("fallback"));
                });
    }

//    @Bean
//    public ReactiveResilience4JCircuitBreakerFactory factory() {
//        return new ReactiveResilience4JCircuitBreakerFactory();
//    }

    // @Bean
    // public Customizer<ReactiveResilience4JCircuitBreakerFactory> slowCustomizer() {
       /* return factory -> factory.configureDefault(
                id -> new ReactiveResilience4JCircuitBreaker(id)
                        .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
                        .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                        .build());*/
        /*return factory -> factory.configure(builder -> {
            return builder
                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(2)).build())
                    // .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    ;
        }, "slow");*/
    // }
}
