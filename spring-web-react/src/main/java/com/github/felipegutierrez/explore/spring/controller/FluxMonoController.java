package com.github.felipegutierrez.explore.spring.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class FluxMonoController {

    List<Integer> list = IntStream.range(0, 3).boxed().collect(Collectors.toList());
    List<Integer> largeList = IntStream.range(0, 20).boxed().collect(Collectors.toList());

    @GetMapping("/flux")
    public Flux<Integer> returnFlux() {
        return Flux
                .fromIterable(list)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> returnFluxStream() {
        return Flux
                .fromIterable(list)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/fluxlargestream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> returnFluxStreamLarge() {
        return Flux
                .fromIterable(largeList)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }
}
