package com.github.felipegutierrez.explore.spring.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

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
        Flux<Double> streamLongFlux = myFluxAndMonoStreams.createFluxDoubleWithDelay(3, 1);

        StepVerifier.create(streamLongFlux)
                .expectSubscription()
                .expectNext(0.0, 1.0, 2.0)
                .verifyComplete();
    }

    /*
    @Test
    void testCreateFluxStreamWithMapAndVerifyDelay() {
        Flux<Double> streamLongFlux = myFluxAndMonoStreams
                .createFluxDoubleWithDelay(4, 1);

        Scheduler scheduler = Schedulers.newSingle("test");
        AtomicInteger incrementer = new AtomicInteger();

        StepVerifier
                .withVirtualTime(() -> streamLongFlux
                        .subscribeOn(scheduler)
                        .doOnNext(value -> incrementer.incrementAndGet())
                )
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(4))
                // .thenConsumeWhile(value -> true)
                // .expectNextCount(4)
                // .expectNext(0.0, 1.0, 2.0, 3.0)
                .verifyComplete()
        ;
    }
     */
}
