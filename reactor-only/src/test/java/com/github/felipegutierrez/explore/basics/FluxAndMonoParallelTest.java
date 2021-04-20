package com.github.felipegutierrez.explore.basics;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class FluxAndMonoParallelTest {

    FluxAndMonoParallel fluxAndMonoParallel = new FluxAndMonoParallel();
    List<String> actualList = IntStream.range(0, 50).boxed()
            .map(String::valueOf)
            .collect(Collectors.toList());

    @Test
    void parallel() {

        var expectList = actualList.stream().map(Integer::parseInt).collect(Collectors.toList());
        var integerParallelFlux = fluxAndMonoParallel.parallel(actualList);

        StepVerifier.create(integerParallelFlux)
                .expectSubscription()
                // .expectNextSequence(expectList) // DOES NOT preserve order because it is parallel
                .expectNextCount(50)
                .verifyComplete();
    }
}