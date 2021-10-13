package com.github.felipegutierrez.explore.advance;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ParallelFlux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class SinksParallelTest {

    @Test
    void sinksParallel() {
        SinksParallel sinksParallel = new SinksParallel();
        var parallelFlux = sinksParallel.start();

        StepVerifier.create(parallelFlux)
                .expectNextCount(20)
                .verifyComplete();
        /*StepVerifier.create(sinksParallel)
                .expectSubscription()
                // .expectNextSequence(expectList) // DOES NOT preserve order because it is parallel
                .expectNextCount(50)
                .verifyComplete();*/
    }
}