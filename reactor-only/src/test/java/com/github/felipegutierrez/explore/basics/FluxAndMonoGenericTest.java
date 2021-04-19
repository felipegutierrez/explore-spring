package com.github.felipegutierrez.explore.basics;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class FluxAndMonoGenericTest {

    FluxAndMonoGeneric<String> myFluxMonoStringGenericTest = new FluxAndMonoGeneric<String>();

    @Test
    void testCreateGenericFluxWithString() {
        List<String> expect = Arrays.asList("Spring", "Spring Boot", "Reactive Spring");
        List<String> expectFallBack = Arrays.asList("Java 8", "Project reactor");
        Flux<String> stringFlux = myFluxMonoStringGenericTest.createFluxConverter(expect, String::toString, expectFallBack);

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete();
    }

    @Test
    void testCreateGenericFluxWithInteger() {
        List<String> expect = Arrays.asList("1", "1234", "7654", "34");
        List<Integer> expectFallBack = Arrays.asList(8, 42);
        Integer[] expectedMessages = new Integer[]{1, 1234, 7654, 34};

        Flux<Integer> integerFlux = myFluxMonoStringGenericTest.createFluxConverter(expect, Integer::parseInt, expectFallBack);

        StepVerifier.create(integerFlux)
                .expectNext(expectedMessages[0])
                .expectNext(expectedMessages[1])
                .expectNext(expectedMessages[2])
                .expectNext(expectedMessages[3])
                .verifyComplete();
    }

    @Test
    void testCreateGenericFluxWithIntegerAndError() {
        List<String> expect = Arrays.asList("1", "1234", "765a", "34");
        Integer[] expectedMessages = new Integer[]{1, 1234};
        List<Integer> expectFallBack = Arrays.asList(8, 42);

        Flux<Integer> integerFlux = myFluxMonoStringGenericTest.createFluxConverter(expect, Integer::parseInt, expectFallBack);

        StepVerifier.create(integerFlux)
                .expectNext(expectedMessages[0])
                .expectNext(expectedMessages[1])
                .expectNextSequence(expectFallBack)
                .verifyComplete()
        // .expectError(NumberFormatException.class) // we are using onErrorResume so we don't expect an error
        // .verify()
        ;
    }

    @Test
    void testCreateGenericFluxWithDouble() {
        List<String> expect = Arrays.asList("1.0", "1234.45", "7654", "34.999");
        List<Double> expectFallBack = Arrays.asList(8.0, 42.5);
        Double[] expectedMessages = new Double[]{1.0, 1234.45, 7654.0, 34.999};

        Flux<Double> doubleFlux = myFluxMonoStringGenericTest.createFluxConverter(expect, Double::parseDouble, expectFallBack);

        StepVerifier.create(doubleFlux)
                .expectNext(expectedMessages[0])
                .expectNext(expectedMessages[1])
                .expectNext(expectedMessages[2])
                .expectNext(expectedMessages[3])
                .verifyComplete();
    }

    @Test
    void testCreateGenericFluxWithDoubleWithError() {
        List<String> expect = Arrays.asList("1.0", "1234.45", "765a", "34.999");
        List<Double> expectFallBack = Arrays.asList(8.0, 42.5);
        Double[] expectedMessages = new Double[]{1.0, 1234.45};

        Flux<Double> doubleFlux = myFluxMonoStringGenericTest.createFluxConverter(expect, Double::parseDouble, expectFallBack);

        StepVerifier.create(doubleFlux)
                .expectNext(expectedMessages[0])
                .expectNext(expectedMessages[1])
                .expectNextSequence(expectFallBack)
                .verifyComplete()
        // .expectError(NumberFormatException.class) // we are using onErrorResume so we don't expect an error
        // .verify()
        ;
    }

    @Test
    void testCreateGenericMonoWithString() {
        String expect = "Spring Boot Reactive Spring";
        Mono<String> stringMono = myFluxMonoStringGenericTest.createMonoConverter(expect, String::toString);

        StepVerifier.create(stringMono)
                .expectNext(expect)
                .verifyComplete();
    }

    @Test
    void testCreateGenericMonoWithInteger() {
        String expect = "1234";
        Mono<Integer> integerMono = myFluxMonoStringGenericTest.createMonoConverter(expect, Integer::parseInt);

        StepVerifier.create(integerMono)
                .expectNext(Integer.valueOf(expect))
                .verifyComplete();
    }

    @Test
    void testCreateGenericMonoWithDouble() {
        String expect = "1234.56";
        Mono<Double> doubleMono = myFluxMonoStringGenericTest.createMonoConverter(expect, Double::parseDouble);

        StepVerifier.create(doubleMono)
                .expectNext(Double.valueOf(expect))
                .verifyComplete();
    }

    @Test
    void testCreateGenericFluxStringWithFilter() {
        List<String> expect = Arrays.asList("java", "jdk", "Spring", "Spring Boot", "Reactive Spring");

        Predicate<String> predicate = n -> n.length() > 4;
        Flux<String> stringFlux = myFluxMonoStringGenericTest.createFluxConverterWithFilter(expect, String::toString, predicate);

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete();
    }

    @Test
    void testCreateGenericFluxDoubleWithFilter() {
        List<String> expect = Arrays.asList("1.0", "1234.45", "7654", "4.999");
        Double[] expectedMessages = new Double[]{1.0, 1234.45, 7654.0, 4.999};

        Predicate<Double> predicate = d -> d > 10.0;
        Flux<Double> doubleFlux = myFluxMonoStringGenericTest.createFluxConverterWithFilter(expect, Double::parseDouble, predicate);

        StepVerifier.create(doubleFlux)
                .expectNext(expectedMessages[1])
                .expectNext(expectedMessages[2])
                .verifyComplete();
    }
}
