package com.github.felipegutierrez.explore.basics;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.felipegutierrez.explore.util.CommonUtil.delay;

@Slf4j
public class FluxAndMonoParallel {
    ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        FluxAndMonoParallel fluxAndMonoParallel = new FluxAndMonoParallel();
        fluxAndMonoParallel.parallel();
        fluxAndMonoParallel.sequential();
    }

    public void parallel() {
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
                .parallel()
                .runOn(Schedulers.fromExecutor(executor))
                .sequential()
                .log();
        longFlux.subscribe();
    }

    public ParallelFlux<Integer> parallel(List<String> numbersList) {
        return Flux.fromIterable(numbersList)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(this::parseIntWithDelay)
                .log();
    }

    public void sequential() {
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
                .parallel()
                .runOn(Schedulers.fromExecutor(executor))
                .sequential()
                .publishOn(Schedulers.single())
                .log();
        longFlux.subscribe();
    }

    private Integer parseIntWithDelay(String s) {
        delay(1000);
        return Integer.parseInt(s);
    }
}
