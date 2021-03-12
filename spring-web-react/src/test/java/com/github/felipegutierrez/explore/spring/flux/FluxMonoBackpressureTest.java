package com.github.felipegutierrez.explore.spring.flux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoBackpressureTest {

    FluxMonoBackpressure fluxMonoBackpressure = new FluxMonoBackpressure();

    @Test
    public void testFluxBackpressure() {
        Flux<Integer> finiteFlux = fluxMonoBackpressure.createFluxFinite(1, 10);
        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .thenRequest(1).expectNext(1)
                .thenRequest(1).expectNext(2)
                .thenRequest(1).expectNext(3)
                .thenCancel()
                .verify();
    }

    @Test
    public void testFluxBackpressureSubscribe() {
        Flux<Integer> finiteFlux = fluxMonoBackpressure.createFluxFinite(1, 10);
        finiteFlux.subscribe(element -> System.out.println("Element is: " + element)
                , ex -> System.err.println("Exception is: " + ex)
                , () -> System.out.println("Done")
                , (subscription -> subscription.request(3))
        );
    }

    @Test
    public void testFluxBackpressureSubscribeCancel() {
        Flux<Integer> finiteFlux = fluxMonoBackpressure.createFluxFinite(1, 10);
        finiteFlux.subscribe(element -> System.out.println("Element is: " + element)
                , ex -> System.err.println("Exception is: " + ex)
                , () -> System.out.println("Done")
                , (subscription -> subscription.cancel())
        );
    }

    @Test
    public void testFluxBackpressureCustomSubscribe() {
        Flux<Integer> finiteFlux = fluxMonoBackpressure.createFluxFinite(1, 10);
        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println("received value: " + value);
                if (value == 4) cancel();
            }
        });
    }
}
