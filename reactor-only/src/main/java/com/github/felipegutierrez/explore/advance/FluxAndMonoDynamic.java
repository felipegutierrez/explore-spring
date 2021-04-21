package com.github.felipegutierrez.explore.advance;

import reactor.core.publisher.Flux;

public class FluxAndMonoDynamic {

    public Flux<Integer> generateFlux() {
        return Flux.generate(() -> 1, (state, integerSynchronousSink) -> {
            integerSynchronousSink.next(state * 3);
            if (state == 10) {
                integerSynchronousSink.complete();
            }
            return state + 1;
        });
    }
}
