package com.github.felipegutierrez.explore.advance;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class FluxAndMonoSubscribe {

    public Flux<Integer> subscribe_01() {
        return Flux.just(1, 2, 3)
                .doOnNext(System.out::println)
                .log()
                .subscribeOn(Schedulers.boundedElastic())
                .log()
                .flatMap(Mono::just);
    }

    public Flux<Integer> subscribe_02() {
        return Flux.just(1, 2, 3)
                .doOnNext(System.out::println)
                .log()
                .flatMap(Mono::just);
    }
}
