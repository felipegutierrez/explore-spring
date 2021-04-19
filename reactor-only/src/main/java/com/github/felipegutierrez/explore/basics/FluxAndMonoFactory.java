package com.github.felipegutierrez.explore.basics;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
public class FluxAndMonoFactory {

    public Flux<String> createFluxUsingIterable(List<String> data) {
        return Flux.fromIterable(data).log();
    }

    public Flux<String> createFluxUsingIterableWithFilter(List<String> data) {
        return Flux
                .fromIterable(data)
                .filter(value -> value.length() > 4)
                .log();
    }

    public Flux<String> createFluxUsingRepeat(List<String> data, Integer repeat) {
        return Flux
                .fromIterable(data)
                .repeat(repeat)
                .log();
    }

    public Flux<String> createFluxUsingArray(String[] data) {
        return Flux.fromArray(data).log();
    }

    public Flux<String> createFluxUsingStream(Stream<String> data) {
        return Flux.fromStream(data).log();
    }

    public Mono<String> createMono(String data) {
        return Mono.justOrEmpty(data).log();
    }

    public Mono<String> createMonoWithSupplier(Supplier<String> data) {
        return Mono.fromSupplier(data).log();
    }

    public Flux<Integer> createFluxFromRange(Integer start, Integer end) {
        return Flux.range(start, end).log();
    }
}
