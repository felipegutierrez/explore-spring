package com.github.felipegutierrez.explore.advance;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FluxAndMonoSubscribeTest {

    FluxAndMonoSubscribe fluxAndMonoSubscribe = new FluxAndMonoSubscribe();

    @Test
    void subscribe_01() {
        var flux = fluxAndMonoSubscribe.subscribe_01();
        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void subscribe_02() {
        var flux = fluxAndMonoSubscribe.subscribe_02();
        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }
}