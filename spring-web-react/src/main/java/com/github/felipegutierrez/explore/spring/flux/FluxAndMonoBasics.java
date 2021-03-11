package com.github.felipegutierrez.explore.spring.flux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FluxAndMonoBasics {

    public static void main(String[] args) {
        FluxAndMonoBasics myFlux = new FluxAndMonoBasics();
        List<String> result = myFlux.createBasicFlux("Spring,Spring Boot,Reactive Spring".split(","));
        result.stream().forEach(System.out::println);
    }

    public List<String> createBasicFlux(String[] data) {
        List<String> result = new ArrayList<String>();

        Flux<String> stringFlux = Flux.just(data).log();

        stringFlux.subscribe(value -> result.add(value));

        return result;
    }

    public List<String> createConcatenateFlux(String[] data01, String[] data02) {
        List<String> result = new ArrayList<String>();

        Flux<String> stringFlux = Flux
                .just(data01)
                .concatWith(Flux.just(data02))
                .log();

        stringFlux.subscribe(value -> result.add(value));

        return result;
    }

    public List<String> createFluxWithComplete(String[] data, String completeMsg) {
        List<String> result = new ArrayList<String>();

        Flux<String> stringFlux = Flux.just(data).log();

        stringFlux.subscribe(value -> result.add(value),
                throwable -> System.out.println("Exception is: " + throwable),
                () -> System.out.println(completeMsg));

        return result;
    }

    public List<Integer> createFluxConverterStringToInt(String[] data) {
        List<Integer> result = new ArrayList<Integer>();

        Flux<Integer> stringFlux = Flux
                .just(data)
                .map(value -> Integer.parseInt(value))
                .log();

        stringFlux.subscribe(value -> result.add(value));

        return result;
    }

    public Flux<String> createBasicFluxWithoutSubscribe(String[] data) {
        List<String> result = new ArrayList<String>();

        Flux<String> stringFlux = Flux.just(data).log();

        return stringFlux;
    }

    public Flux<Integer> createFluxWithoutSubscribeConverterStringToInt(String[] data) {
        Flux<Integer> stringFlux = Flux
                .just(data)
                .map(value -> Integer.parseInt(value))
                .log();
        return stringFlux;
    }

    public Mono<Integer> createMonoConverterStringToInt(String data) {
        Mono<Integer> integerMono = Mono.just(data)
                .map(value -> Integer.parseInt(value))
                .log();
        return integerMono;
    }

//    public <OUT> Mono<OUT> createMonoConverterString(String data) {
//        // Class<OUT> clazz = new Class<OUT>();
//        // OUT instance = clazz.newInstance();
//        // OUT instance = ((Class)((OUT)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();;
//        OUT instance;
//        Mono<OUT> integerMono = Mono.just(data)
//                // .map(value -> Integer.parseInt(value))
//                // .map(value -> clazz.cast(value) )
//                .map(value -> instance.getClass().cast(value))
//                .log();
//
//        return integerMono;
//    }
}
