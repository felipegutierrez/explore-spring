package com.github.felipegutierrez.explore.spring.handler;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class SampleHandlerFunctionTest {

    @Autowired
    WebTestClient webTestClient;

    SampleHandlerFunction sampleHandlerFunction = new SampleHandlerFunction();

    @Test
    public void fluxTest_approach1() {
        Flux<Integer> integerFlux = webTestClient
                .get().uri("/functional/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNextSequence(sampleHandlerFunction.list)
                .verifyComplete();
    }

    @Test
    public void fluxTest_approach2() {
        webTestClient
                .get().uri("/functional/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(sampleHandlerFunction.list.size());
    }

    @Test
    public void fluxTest_approach3() {
        EntityExchangeResult<List<Integer>> entityExchangeResult = webTestClient
                .get().uri("/functional/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();
        assertEquals(sampleHandlerFunction.list, entityExchangeResult.getResponseBody());
    }

    @Test
    public void fluxTest_approach4() {
        webTestClient
                .get().uri("/functional/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith(response -> assertEquals(sampleHandlerFunction.list, response.getResponseBody()));
    }

    @Test
    public void monoTest_approach1() {
        webTestClient
                .get().uri("/functional/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(response -> assertEquals(Integer.valueOf(1), response.getResponseBody()));
    }

    @Test
    public void fluxStreamTest_approach1() {
        Flux<Long> longFlux = webTestClient
                .get().uri("/functional/fluxstream")
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
}
