package com.github.felipegutierrez.explore.basics;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FluxAndMonoTransformationsTest {

    FluxAndMonoTransformations fluxAndMonoTransformations = new FluxAndMonoTransformations();

    List<String> expect = Arrays.asList(
            "Spring", "Spring Boot", "Reactive Spring",
            "java 8", "reactive programming", "java with lambda",
            "Scala", "Scala rocks", "but Java is catching up"
    );
    List<String> list1 = Arrays.asList("A", "B", "C");
    List<String> list2 = Arrays.asList("D", "E", "F");

    @Test
    void testFluxUsingIterable() {
        Flux<String> stringFlux = fluxAndMonoTransformations.createFluxUsingFlatMap(expect);

        StepVerifier.create(stringFlux)
                .expectNextCount(18)
                .verifyComplete();
    }

    @Test
    void testFluxUsingIterableParallel() {
        Flux<String> stringFlux = fluxAndMonoTransformations.createFluxUsingFlatMapParallel(expect, 2);
        StepVerifier.create(stringFlux)
                .expectNextCount(18)
                .verifyComplete();
    }

    @Test
    void testFluxUsingIterableParallelMaintainOrder() {
        Flux<String> stringFlux = fluxAndMonoTransformations.createFluxUsingFlatMapParallelMaintainOrder(expect, 2);
        StepVerifier.create(stringFlux)
                .expectNextCount(18)
                .verifyComplete();
    }

    @Test
    void testFluxUsingMerge() {
        Flux<String> mergeFlux = fluxAndMonoTransformations.createFluxUsingMerge(list1, list2);
        List<String> expect = Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList());
        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextSequence(expect)
                .verifyComplete();
    }

    @Test
    void testFluxUsingMergeDelay() {
        Flux<String> mergeFlux = fluxAndMonoTransformations.createFluxUsingMergeDelay(list1, list2);
        var expect = Arrays.asList("A", "D", "B", "E", "C", "F");

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextSequence(expect) // merge with delay does NOT preserve order
                .verifyComplete();

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextCount(expect.size())
                .verifyComplete();
    }

    @Test
    void testFluxUsingMergeWith() {
        Flux<String> mergeFlux = fluxAndMonoTransformations.createFluxUsingMergeWith(list1, list2);
        List<String> expect = Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList());
        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextSequence(expect)
                .verifyComplete();
    }

    @Test
    void testFluxUsingMergeWithDelay() {
        var mergeFlux = fluxAndMonoTransformations.createFluxUsingMergeWithDelay(list1, list2);
        var expect = Arrays.asList("A", "D", "B", "E", "C", "F");

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextCount(expect.size())
                .verifyComplete();

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextSequence(expect) // merge with delay does NOT preserve order
                .verifyComplete();
    }

    @Test
    void testFluxUsingMergeSequentialWithDelay() {
        var mergeFlux = fluxAndMonoTransformations.createFluxUsingMergeSequentialWithDelay(list1, list2);
        var expect = Arrays.asList("A", "B", "C", "D", "E", "F");

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextCount(expect.size())
                .verifyComplete();

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextSequence(expect) // merge sequential with delay still DOES preserve order
                .verifyComplete();
    }

    @Test
    void testFluxUsingMergeMonoWithDelay() {
        var mergeFlux = fluxAndMonoTransformations.createFluxUsingMonoMergeWithDelay("A", "B");
        var expect = Arrays.asList("A", "B");

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextCount(expect.size())
                .verifyComplete();

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextSequence(expect) // merge with delay does NOT preserve order
                .verifyComplete();
    }

    @Test
    void testFluxUsingConcat() {
        Flux<String> concatFlux = fluxAndMonoTransformations.createFluxUsingConcat(list1, list2);
        List<String> expect = Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList());
        StepVerifier.create(concatFlux)
                .expectSubscription()
                .expectNextSequence(expect)
                .verifyComplete();
    }

    @Test
    void testFluxUsingConcatWithDelay() {
        Flux<String> concatFlux = fluxAndMonoTransformations.createFluxUsingConcatWithDelay(list1, list2);
        List<String> expect = Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList());

        StepVerifier.create(concatFlux)
                .expectSubscription()
                .expectNextSequence(expect) // concat with delay DOES preserve order
                .verifyComplete();

        StepVerifier.create(concatFlux)
                .expectSubscription()
                .expectNextCount(expect.size()) // if we use expectNextSequence() we cannot use another expectNextCount
                .verifyComplete();
    }

    @Test
    void testFluxUsingZip() {
        Flux<String> zipFlux = fluxAndMonoTransformations.createFluxUsingZip(list1, list2);
        List<String> expect = Arrays.asList("AD", "BE", "CF");
        StepVerifier.create(zipFlux)
                .expectSubscription()
                .expectNextSequence(expect)
                .verifyComplete();
    }

    @Test
    void testFluxUsingGroupBy() {
        int numberOfPartitions = 3;
        int maxCount = 100;
        Flux<GroupedFlux<Integer, Data>> dataGroupedFlux = fluxAndMonoTransformations.createFluxUsingGroupBy(expect, numberOfPartitions, maxCount);
        StepVerifier.create(dataGroupedFlux)
                .expectNextCount(numberOfPartitions)
                .verifyComplete();
    }

    @Test
    void testFluxUsingHashGroupBy() {
        int numberOfPartitions = 3;
        int parallelism = 2;
        int maxCount = 100;
        Flux<GroupedFlux<Integer, Data>> dataGroupedFlux = fluxAndMonoTransformations.createFluxUsingHashGroupBy(expect, numberOfPartitions, parallelism, maxCount);
        StepVerifier.create(dataGroupedFlux)
                .expectNextCount(numberOfPartitions)
                .verifyComplete();
    }
}
