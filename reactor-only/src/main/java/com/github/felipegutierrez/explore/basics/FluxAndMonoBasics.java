package com.github.felipegutierrez.explore.basics;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

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

    public Collection<String> createBasicFluxToMap(String data) {
        return Flux.just(data)
                .collectMap(value -> {
                            return value;
                        },
                        value -> {
                            return value;
                        })
                .block()
                .values();
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

    public Flux<String> createBasicFluxWithFlatmap(List<String> data) {
        Flux<String> stringFlux = Flux
                .fromIterable(data)
                .flatMap(value -> Flux.fromArray(value.split("")))
                .log();
        return stringFlux;
    }

    public Flux<String> createBasicFluxWithFlatmapDelay(List<String> data) {
        Flux<String> stringFlux = Flux
                .fromIterable(data)
                .flatMap(value -> {
                            var delay = new Random().nextInt(1000);
                            return Flux
                                    .fromArray(value.split(""))
                                    .delayElements(Duration.ofMillis(delay));
                        }
                )
                .log();
        return stringFlux;
    }

    public Flux<String> createBasicFluxWithConcatmapDelay(List<String> data) {
        Flux<String> stringFlux = Flux
                .fromIterable(data)
                .concatMap(value -> {
                            var delay = new Random().nextInt(1000);
                            return Flux
                                    .fromArray(value.split(""))
                                    .delayElements(Duration.ofMillis(delay));
                        }
                )
                .log();
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

    public Flux<Integer> createFluxFromMono(String data) {
        Flux<Integer> integerFlux = Mono.just(data)
                .flatMapMany(value -> {
                    var arrayOfStrings = value.split("");
                    return Flux.fromIterable(
                            Arrays.stream(arrayOfStrings)
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList())
                    );
                })
                .log();
        return integerFlux;
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
