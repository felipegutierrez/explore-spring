package com.github.felipegutierrez.explore.advance;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoDynamicTest {

    FluxAndMonoDynamic fluxAndMonoDynamic = new FluxAndMonoDynamic();

    @Test
    void generateFlux() {
        var integerFlux = fluxAndMonoDynamic.generateFlux().log();
        StepVerifier.create(integerFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void createAsyncFlux() {
        var list = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        var integerFlux = fluxAndMonoDynamic.createAsyncFlux(list).log();
        StepVerifier.create(integerFlux)
                .expectNextCount(10)
                .verifyComplete();
    }
}