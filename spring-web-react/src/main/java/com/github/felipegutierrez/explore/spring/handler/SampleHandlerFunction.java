package com.github.felipegutierrez.explore.spring.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * comment implementation 'org.springframework.boot:spring-boot-starter-data-mongodb-reactive' at build.gradle in order to this application work
 */
@Component
public class SampleHandlerFunction {

    List<Integer> list = IntStream.range(0, 3).boxed().collect(Collectors.toList());

    public Mono<ServerResponse> flux(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Flux.fromIterable(list).log(),
                        Integer.class
                );
    }

    public Mono<ServerResponse> fluxStream(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(
                        Flux.interval(Duration.ofSeconds(1)).log(),
                        Long.class
                );
    }

    public Mono<ServerResponse> mono(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.justOrEmpty(Integer.valueOf(1)).log(),
                        Integer.class
                );
    }
}
