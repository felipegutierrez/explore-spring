package com.github.felipegutierrez.explore.spring.flux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Slf4j
public class FluxAndMonoExceptionHandling {
    public Flux<String> createFluxErrorHandling(List<String> data, List<String> resumeList) {
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
}
