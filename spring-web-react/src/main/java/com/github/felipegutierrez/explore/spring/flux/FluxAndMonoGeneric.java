package com.github.felipegutierrez.explore.spring.flux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
public class FluxAndMonoGeneric<IN> {

    public <OUT> Flux<OUT> createFluxConverter(List<IN> data, Function<IN, OUT> mapper, List<OUT> fallbackData) {
        Flux<OUT> fluxResult = Flux
                .fromIterable(data)
                .map(mapper)
                .onErrorResume(e -> {
                    log.warn("an error occurred but I will make sure that this flux completes: {}", e);
                    return Flux.fromIterable(fallbackData);
                })
                .log();
        return fluxResult;
    }

    public <OUT> Mono<OUT> createMonoConverter(IN data, Function<IN, OUT> mapper) {
        Mono<OUT> monoResult = Mono.just(data)
                .map(mapper)
                .log();
        return monoResult;
    }

    public <OUT> Flux<OUT> createFluxConverterWithFilter(List<IN> data,
                                                         Function<IN, OUT> mapper,
                                                         Predicate<OUT> predicate) {
        Flux<OUT> fluxResult = Flux
                .fromIterable(data)
                .map(mapper)
                .filter(predicate)
                .log();
        return fluxResult;
    }
}
