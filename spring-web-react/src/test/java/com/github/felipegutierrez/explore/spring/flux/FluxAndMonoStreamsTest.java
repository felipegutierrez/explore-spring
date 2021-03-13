package com.github.felipegutierrez.explore.spring.flux;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class FluxAndMonoStreamsTest {

    FluxAndMonoStreams myFluxAndMonoStreams = new FluxAndMonoStreams();

    @Test
    void testCreateFluxStream() {
        Flux<Long> streamLongFlux = myFluxAndMonoStreams.createFluxLong(100, 3);

        StepVerifier.create(streamLongFlux)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

    @Test
    void testCreateFluxStreamWithMap() {
        Flux<Double> streamLongFlux = myFluxAndMonoStreams.createFluxDouble(100, 3);

        StepVerifier.create(streamLongFlux)
                .expectSubscription()
                .expectNext(0.0, 1.0, 2.0)
                .verifyComplete();
    }

    @Test
    void testCreateFluxStreamWithMapAndDelay() {
        Flux<Double> streamLongFlux = myFluxAndMonoStreams.createFluxDoubleWithDelay(4, 1);

        StepVerifier.create(streamLongFlux)
                .expectSubscription()
                .expectNext(0.0, 1.0, 2.0, 3.0)
                .verifyComplete();
    }

    @Test
    void testCreateFluxStreamWithMapAndDelayWithVirtualTime() {
        VirtualTimeScheduler.getOrSet();
        Flux<Double> streamLongFlux = myFluxAndMonoStreams.createFluxDoubleWithDelay(10, 1);

        StepVerifier.withVirtualTime(() -> streamLongFlux)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(10))
                .expectNext(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)
                .verifyComplete();
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCreateFluxStreamWithMapAndVerifyDelay() {
        Flux<Double> streamLongFlux = myFluxAndMonoStreams
                .createFluxDoubleWithDelay(4, 1);

        StepVerifier.create(streamLongFlux)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(4))
                .expectNext(0.0, 1.0, 2.0, 3.0)
                .verifyComplete();
    }
}
