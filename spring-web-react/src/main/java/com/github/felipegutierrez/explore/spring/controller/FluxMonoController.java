package com.github.felipegutierrez.explore.spring.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * comment implementation 'org.springframework.boot:spring-boot-starter-data-mongodb-reactive' at build.gradle in order to this application work
 */
@RestController
public class FluxMonoController {

    List<Integer> list = IntStream.range(0, 3).boxed().collect(Collectors.toList());

    @GetMapping("/mono")
    public Mono<Integer> returnMono() {
        return Mono.just(1)
                .log();
    }

    @GetMapping("/flux")
    public Flux<Integer> returnFlux() {
        return Flux
                .fromIterable(list)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> returnFluxStream() {
        return Flux
                .interval(Duration.ofSeconds(1))
                .log();
    }
}
