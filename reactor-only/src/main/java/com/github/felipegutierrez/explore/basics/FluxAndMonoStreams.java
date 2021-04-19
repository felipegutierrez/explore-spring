package com.github.felipegutierrez.explore.basics;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class FluxAndMonoStreams {
    public Flux<Long> createFluxLong(long millis, long numberOfElements) {
        return Flux.interval(Duration.ofMillis(millis))
                .take(numberOfElements)
                .log();
    }

    public Flux<Double> createFluxDouble(long millis, long numberOfElements) {
        return Flux.interval(Duration.ofMillis(millis))
                .map(l -> l.doubleValue())
                .take(numberOfElements)
                .log();
    }

    public Flux<Double> createFluxDoubleWithDelay(long numberOfElements, long delaySec) {
        return Flux.interval(Duration.ofSeconds(delaySec))
                // .delayElements(Duration.ofSeconds(delaySec))
                .map(l -> l.doubleValue())
                .take(numberOfElements)
                .log();
    }
}
