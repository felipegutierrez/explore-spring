package com.github.felipegutierrez.explore.basics;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoExceptionHandlingTest {

    FluxAndMonoExceptionHandling fluxAndMonoExceptionHandling = new FluxAndMonoExceptionHandling();
    List<String> expect = Arrays.asList("Spring", "Spring Boot", "Reactive Spring");
    List<String> resumeList = Arrays.asList("Y", "M", "C", "A");

    @Test
    public void testFluxErrorHandling() {
        Flux<String> stringFlux = fluxAndMonoExceptionHandling.createFluxErrorHandlingGuaranteeCompletion(expect, resumeList);
        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNextSequence(expect)
                // .expectError(RuntimeException.class)
                .expectNextSequence(resumeList)
                .verifyComplete();
    }

    @Test
    public void testFluxErrorHandlingOnMap() {
        Flux<String> stringFlux = fluxAndMonoExceptionHandling.createFluxErrorHandlingOnMap(expect, resumeList);
        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNextSequence(expect)
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void testFluxErrorHandlingOnMapWithExceptionMessage() {
        Flux<String> stringFlux = fluxAndMonoExceptionHandling.createFluxErrorHandlingOnMap(expect, resumeList);
        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNextSequence(expect)
                .expectErrorMessage("an exception occurred")
                .verify();
    }

    @Test
    public void testFluxErrorHandlingOnMapWithRetry() {
        Flux<String> stringFlux = fluxAndMonoExceptionHandling.createFluxErrorHandlingOnMapRetry(expect, resumeList, 2);
        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNextSequence(expect)
                .expectNextSequence(expect)
                .expectNextSequence(expect)
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void testFluxErrorHandlingOnMapWithRetryBackoff() {
        Flux<String> stringFlux = fluxAndMonoExceptionHandling.createFluxErrorHandlingOnMapRetryBackoff(expect, resumeList, 2);
        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNextSequence(expect)
                .expectNextSequence(expect)
                .expectNextSequence(expect)
                .expectError(IllegalStateException.class)
                .verify();
    }
}
