package com.github.felipegutierrez.explore.spring.cloud.controllers;

import java.util.HashMap;
import java.util.Map;

import com.github.felipegutierrez.explore.spring.cloud.services.CompletableFutureHttpBinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompletableFutureDemoController {

    Logger LOG = LoggerFactory.getLogger(CompletableFutureDemoController.class);

    private CompletableFutureHttpBinService httpBin;
    private ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    public CompletableFutureDemoController(CompletableFutureHttpBinService httpBin, ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory) {
        this.httpBin = httpBin;
        this.reactiveCircuitBreakerFactory = reactiveCircuitBreakerFactory;
    }

    @GetMapping("/completablefuture/get")
    public Mono<Map> get() {
        return Mono.fromFuture(httpBin.get());
    }

    @GetMapping("/completablefuture/delay/{seconds}")
    public Mono<Map> delay(@PathVariable int seconds) {
        return reactiveCircuitBreakerFactory.create("completablefuturedelay").run(Mono.fromFuture(httpBin.delay(seconds)), t -> {
            LOG.warn("delay call failed error", t);
            Map<String, String> fallback = new HashMap();
            fallback.put("hello", "world");
            return Mono.just(fallback);
        });
    }
}
