package com.github.felipegutierrez.explore.basics;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Slf4j
public class FluxAndMonoExceptionHandling {
    public Flux<String> createFluxErrorHandlingGuaranteeCompletion(List<String> data, List<String> resumeList) {
        return Flux
                .fromIterable(data)
                .concatWith(Flux.error(new RuntimeException("an exception occurred")))
                .concatWith(Flux.just("D"))
                .onErrorResume(e -> {
                    System.out.println("exception: " + e);
                    return Flux.fromIterable(resumeList);
                })
                .log();
    }

    public Flux<String> createFluxErrorContinueGuaranteeCompletion(List<String> data, List<String> resumeList) {
        return Flux
                .fromIterable(data)
                .map(value -> {
                    if (value.equalsIgnoreCase("Spring Boot")) throw new RuntimeException("an exception occurred");
                    return value;
                })
                .concatWith(Flux.fromIterable(resumeList))
                .onErrorContinue((ex, value) -> {
                    log.error("Exception is: {}", ex);
                    log.info("value is: {}", value);
                })
                .log();
    }

    public Flux<String> createFluxErrorHandlingOnMap(List<String> data, List<String> anotherData) {
        return Flux
                .fromIterable(data)
                .concatWith(Flux.error(new RuntimeException("an exception occurred")))
                .concatWith(Flux.fromIterable(anotherData))
                .onErrorMap(e -> new CustomException(e))
                .log();
    }

    public Flux<String> createFluxErrorHandlingOnMapRetry(List<String> data, List<String> anotherData, long maxAttempts) {
        return Flux
                .fromIterable(data)
                .concatWith(Flux.error(new RuntimeException("an exception occurred")))
                .concatWith(Flux.fromIterable(anotherData))
                .onErrorMap(e -> new CustomException(e))
                .retry(maxAttempts)
                .log();
    }

    public Flux<String> createFluxErrorHandlingOnMapRetryBackoff(List<String> data, List<String> anotherData, long maxAttempts) {
        return Flux
                .fromIterable(data)
                .concatWith(Flux.error(new RuntimeException("an exception occurred")))
                .concatWith(Flux.fromIterable(anotherData))
                .onErrorMap(e -> new CustomException(e))
                .retryWhen(Retry.backoff(maxAttempts, Duration.ofSeconds(2)))
                .log();
    }

    public Mono<Integer> createMonoIntegerParse(String value) {
        return Mono.just(value)
                .map(Integer::parseInt)
                .onErrorReturn(0)
                .log();
    }

    public Mono<Integer> createMonoIntegerParseOnErrorContinue(String value) {
        return Mono.just(value)
                .map(Integer::parseInt)
                .onErrorContinue((ex, v) -> {
                    log.error("Exception is: {}", ex);
                    log.info("value is: {}", v);
                })
                .log();
    }
}
