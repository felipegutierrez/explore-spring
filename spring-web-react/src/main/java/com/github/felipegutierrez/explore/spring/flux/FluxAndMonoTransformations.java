package com.github.felipegutierrez.explore.spring.flux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static reactor.core.scheduler.Schedulers.parallel;

@Slf4j
public class FluxAndMonoTransformations {

    public Flux<String> createFluxUsingFlatMap(List<String> data) {
        return Flux
                .fromIterable(data)
                .flatMap(value -> Flux.fromIterable(simulateDataBaseAccess(value)))
                .log();
    }

    public Flux<String> createFluxUsingFlatMapParallel(List<String> data, Integer windowSize) {
        return Flux
                .fromIterable(data)
                .window(windowSize)
                .flatMap(listValue -> listValue.map(this::simulateDataBaseAccess).subscribeOn(parallel()))
                .flatMap(listValue -> Flux.fromIterable(listValue))
                .log();
    }

    public Flux<String> createFluxUsingFlatMapParallelMaintainOrder(List<String> data, Integer windowSize) {
        return Flux
                .fromIterable(data)
                .window(windowSize)
                .flatMapSequential(listValue -> listValue.map(this::simulateDataBaseAccess).subscribeOn(parallel()))
                .flatMap(listValue -> Flux.fromIterable(listValue))
                .log();
    }

    private List<String> simulateDataBaseAccess(String value) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(value, UUID.randomUUID().toString());
    }
}
