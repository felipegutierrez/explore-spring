package com.github.felipegutierrez.explore.spring.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
public class FluxMonoControllerTest {

    /**
     * This test is not working anymore because ItemController replaced
     * it with the ItemReactiveRepository.
     */
    @Autowired
    WebTestClient webTestClient;

    FluxMonoController fluxMonoController = new FluxMonoController();

    @Test
    public void monoTest_approach1() {
        webTestClient
                .get().uri("/mono")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(response -> assertEquals(Integer.valueOf(1), response.getResponseBody()));
    }

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
        Flux<Long> longFlux = webTestClient
                .get().uri("/fluxstream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(longFlux)
                .expectSubscription()
                .expectNext(0l)
                .expectNext(1l)
                .expectNext(2l)
                .thenCancel()
                .verify();
    }

/*
    @Test
    public void fluxStreamTest_approach2() {
        VirtualTimeScheduler.getOrSet();
        Flux<Long> longFlux = webTestClient
                .get().uri("/fluxstream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(longFlux)
                .expectSubscription()
                .expectNext(0l)
                .expectNext(1l)
                .expectNext(2l)
                .thenAwait(Duration.ofSeconds(3))
                .thenCancel()
                .verify();
    }
*/
}
