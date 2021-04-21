package com.github.felipegutierrez.explore.advance;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FluxAndMonoDynamic {

    /**
     * the generate operator is synchronous
     *
     * @return
     */
    public Flux<Integer> generateFlux() {
        return Flux.generate(() -> 1, (state, integerSynchronousSink) -> {
            integerSynchronousSink.next(state * 3);
            if (state == 10) {
                integerSynchronousSink.complete();
            }
            return state + 1;
        });
    }

    public Flux<Integer> createAsyncFlux(List<String> names) {
        return Flux.create(fluxSink -> {
            // synchronous
            // names.stream().map(Integer::parseInt).forEach(sink::next);
            // sink.complete();

            // asynchronous
            CompletableFuture.supplyAsync(() -> names)
                    .thenAccept(n -> n.stream().map(Integer::parseInt).forEach(fluxSink::next))
                    .thenRun(fluxSink::complete);
        });
    }

    public Mono<Integer> createAsyncMono(String name) {
        return Mono.create(monoSink -> {
            monoSink.success(Integer.parseInt(name));
        });
    }
}
