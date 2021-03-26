package com.github.felipegutierrez.explore.spring.flux;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

enum MyThingEnum {
    A
}

class MyThing {
    public MyThingEnum status;
}

public class FluxFlatMapWithFilter {

    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Mono<MyThing> myThingMono = getMyThingMono();
        return myThingMono.flatMap(thing -> {
            switch (thing.status) {
                case A:
                    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                    return exchange.getResponse().setComplete();
                default:
                    exchange.getResponse().setStatusCode(HttpStatus.OK);
                    return chain.filter(exchange);
            }
        })
                .switchIfEmpty(Mono.empty());
    }

    private Mono<MyThing> getMyThingMono() {
        return Mono.just(new MyThing());
    }
}
