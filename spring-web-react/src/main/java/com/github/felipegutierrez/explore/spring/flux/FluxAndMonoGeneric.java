package com.github.felipegutierrez.explore.spring.flux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Slf4j
public class FluxAndMonoGeneric<IN> {

    public <OUT> Flux<OUT> createFluxConverter(List<IN> data, Function<IN, OUT> mapper) {
        Flux<OUT> fluxResult = Flux
                .fromIterable(data)
                .map(mapper)
                .log();
        return fluxResult;
    }

    public <OUT> Mono<OUT> createMonoConverter(IN data, Function<IN, OUT> mapper) {
        Mono<OUT> monoResult = Mono.just(data)
                .map(mapper)
                .log();
        return monoResult;
    }
}
