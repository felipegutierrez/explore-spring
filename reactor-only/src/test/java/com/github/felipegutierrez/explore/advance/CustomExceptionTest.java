package com.github.felipegutierrez.explore.advance;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * https://stackoverflow.com/a/68396995/2096986
 */
public class CustomExceptionTest {

    private final Mono<Integer> errorMono = Mono.error(() -> new Exception("Empty"));

    @Test
    public void testErrors() {
        function1(null)
                .as(StepVerifier::create)
                .consumeErrorWith(Throwable::printStackTrace)
                .verify();
        function2(null)
                .as(StepVerifier::create)
                .consumeErrorWith(Throwable::printStackTrace)
                .verify();
        function3(null)
                .as(StepVerifier::create)
                .consumeErrorWith(Throwable::printStackTrace)
                .verify();
    }

    private Mono<Integer> function1(Integer input) {
        return Mono.justOrEmpty(input)
                .doOnNext(i -> System.out.println("Function 1 " + i))
                .switchIfEmpty(Mono.error(() -> new Exception("function 1")));
    }

    private Mono<Integer> function2(Integer input) {
        return Mono.justOrEmpty(input)
                .doOnNext(i -> System.out.println("Function 2 " + i))
                .switchIfEmpty(Mono.error(() -> new Exception("function 2")));
    }

    private Mono<Integer> function3(Integer input) {
        return Mono.justOrEmpty(input)
                .doOnNext(i -> System.out.println("Function 3 " + i))
                .switchIfEmpty(Mono.error(() -> new Exception("function 3")));
    }
}
