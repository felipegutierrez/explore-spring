package com.github.felipegutierrez.explore.advance;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FluxAndMonoDynamicTest {

    FluxAndMonoDynamic fluxAndMonoDynamic = new FluxAndMonoDynamic();

    @Test
    void generateFlux() {
        var integerFlux = fluxAndMonoDynamic.generateFlux().log();
        StepVerifier.create(integerFlux)
                .expectNextCount(10)
                .verifyComplete();
    }
}