package com.github.felipegutierrez.explore.spring.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoTransformationsTest {

    FluxAndMonoTransformations fluxAndMonoFactory = new FluxAndMonoTransformations();

    List<String> expect = Arrays.asList(
            "Spring", "Spring Boot", "Reactive Spring",
            "java 8", "reactive programming", "java with lambda",
            "Scala", "Scala rocks", "but Java is catching up"
    );

    @Test
    void testFluxUsingIterable() {
        Flux<String> stringFlux = fluxAndMonoFactory.createFluxUsingFlatMap(expect);

        StepVerifier.create(stringFlux)
                .expectNextCount(18)
                .verifyComplete();
    }

    @Test
    void testFluxUsingIterableParallel() {
        Flux<String> stringFlux = fluxAndMonoFactory.createFluxUsingFlatMapParallel(expect, 2);
        StepVerifier.create(stringFlux)
                .expectNextCount(18)
                .verifyComplete();
    }

    @Test
    void testFluxUsingIterableParallelMaintainOrder() {
        Flux<String> stringFlux = fluxAndMonoFactory.createFluxUsingFlatMapParallelMaintainOrder(expect, 2);
        StepVerifier.create(stringFlux)
                .expectNextCount(18)
                .verifyComplete();
    }
}
