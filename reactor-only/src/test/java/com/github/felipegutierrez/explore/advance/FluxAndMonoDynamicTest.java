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

    @Test
    void createAsyncMono() {
        var integerMono = fluxAndMonoDynamic.createAsyncMono("135");
        StepVerifier.create(integerMono)
                .expectNext(135)
                .verifyComplete();
    }

    @Test
    void handleFlux() {
        var list = List.of("00", "10", "224", "325", "4456", "5780", "65", "771", "808", "905");
        var integerFlux = fluxAndMonoDynamic.handleFlux(list).log();
        StepVerifier.create(integerFlux)
                .expectNextCount(7)
                .verifyComplete();
    }
}
