package com.github.felipegutierrez.explore.advance;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class FluxMonoBackpressure {

    public Flux<Integer> createFluxFinite(Integer start, Integer end) {
        return Flux
                .range(start, end)
                .log();
    }
}
