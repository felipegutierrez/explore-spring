package com.github.felipegutierrez.explore.spring.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@WebFluxTest
public class FluxMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    FluxMonoController fluxMonoController = new FluxMonoController();

    @Test
    public void fluxTest_approach1() {
        Flux<Integer> integerFlux = webTestClient
                .get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNextSequence(fluxMonoController.list)
                .verifyComplete();
    }

    @Test
    public void fluxTest_approach2() {
        webTestClient
                .get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .hasSize(fluxMonoController.list.size());
    }

    @Test
    public void fluxTest_approach3() {
        EntityExchangeResult<List<Integer>> entityExchangeResult = webTestClient
                .get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();
        assertEquals(fluxMonoController.list, entityExchangeResult.getResponseBody());
    }

    @Test
    public void fluxTest_approach4() {
        webTestClient
                .get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith(response -> assertEquals(fluxMonoController.list, response.getResponseBody()));
    }

    @Test
    public void fluxStreamTest_approach1() {
        Flux<Integer> integerFlux = webTestClient
                .get().uri("/fluxstream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNextSequence(fluxMonoController.list)
                .verifyComplete();
    }

    @Test
    public void fluxStreamTest_approach2() {
        webTestClient
                .get().uri("/fluxstream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON)
                .expectBodyList(Integer.class)
                .hasSize(fluxMonoController.list.size());
    }

    @Test
    public void fluxStreamTest_approach3() {
        EntityExchangeResult<List<Integer>> entityExchangeResult = webTestClient
                .get().uri("/fluxstream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();
        assertEquals(fluxMonoController.list, entityExchangeResult.getResponseBody());
    }

    @Test
    public void fluxStreamTest_approach4() {
        webTestClient
                .get().uri("/fluxstream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith(response -> assertEquals(fluxMonoController.list, response.getResponseBody()));
    }

    /*
    @Test
    public void fluxLargeTest_approach1() {
        VirtualTimeScheduler.getOrSet();
        Flux<Integer> integerFlux = webTestClient
                .get().uri("/fluxlargestream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(20))
                .expectNextSequence(fluxMonoController.largeList)
                .verifyComplete();
    }
    */
}
