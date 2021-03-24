package com.github.felipegutierrez.explore.spring.handlers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class LibraryEventsFunctionHandler {

    List<Integer> list = IntStream.range(0, 3).boxed().collect(Collectors.toList());

    /**
     * "http POST http://localhost:8080/v1/func/libraryevent < spring-kafka-library-producer/src/main/resources/static/libraryEvent-00.json"
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> postLibraryEvent(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Flux.fromIterable(list).log(), Integer.class);
    }
}
