package com.github.felipegutierrez.explore.spring.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FluxAndMonoFactoryTest {

    FluxAndMonoFactory fluxAndMonoFactory = new FluxAndMonoFactory();

    @Test
    void testFluxUsingIterable() {
        List<String> expect = Arrays.asList("Spring", "Spring Boot", "Reactive Spring");
        Flux<String> stringFlux = fluxAndMonoFactory.createFluxUsingIterable(expect);
        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
                .verifyComplete();
    }

    @Test
    void testFluxUsingArray() {
        String[] expect = new String[]{"Spring", "Spring Boot", "Reactive Spring"};
        Flux<String> stringFlux = fluxAndMonoFactory.createFluxUsingArray(expect);
        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
                .verifyComplete();
    }

    @Test
    void testFluxUsingStream() {
        Stream<String> expect = Stream.of("Spring", "Spring Boot", "Reactive Spring");
        Flux<String> stringFlux = fluxAndMonoFactory.createFluxUsingStream(expect);
        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
                .verifyComplete();
    }

    @Test
    void testMono() {
        String expect = "Spring Boot Reactive Spring";
        Mono<String> stringFlux = fluxAndMonoFactory.createMono(expect);
        StepVerifier.create(stringFlux)
                .expectNext(expect)
                .verifyComplete();
    }

    @Test
    void testEmptyMono() {
        Mono<String> stringFlux = fluxAndMonoFactory.createMono(null);
        StepVerifier.create(stringFlux)
                .verifyComplete();
    }

    @Test
    void testMonoWithSupplier() {
        Supplier<String> expect = () -> "Spring Boot Reactive Spring";
        Mono<String> stringFlux = fluxAndMonoFactory.createMonoWithSupplier(expect);
        StepVerifier.create(stringFlux)
                .expectNext(expect.get())
                .verifyComplete();
    }

    @Test
    void testFluxFromRange() {
        Integer start = 0;
        Integer end = 100;
        List<Integer> range = IntStream.range(start, end).boxed().collect(Collectors.toList());
        Flux<Integer> rangeFlux = fluxAndMonoFactory.createFluxFromRange(start, end);
        StepVerifier.create(rangeFlux)
                .expectNextSequence(range)
                .verifyComplete();
    }

    @Test
    void testFluxUsingIterableWithFilter() {
        List<String> expect = Arrays.asList("java", "jdk", "Spring", "Spring Boot", "Reactive Spring");
        Flux<String> stringFlux = fluxAndMonoFactory.createFluxUsingIterableWithFilter(expect);

        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
                .verifyComplete();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .verifyComplete();
    }
}
