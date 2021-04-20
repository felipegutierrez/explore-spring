package com.github.felipegutierrez.explore.basics;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class FluxAndMonoBasics {

    final Function<Flux<String>, Flux<Integer>> integerToStringFluxTransformation = value -> value
            .flatMap(v -> {
                var arrayOfStrings = v.split("");
                return Flux
                        .fromIterable(Arrays
                                .stream(arrayOfStrings)
                                .map(Integer::parseInt)
                                .collect(Collectors.collectingAndThen(Collectors.toList(), c -> !c.isEmpty() ? c : new ArrayList<Integer>()))
                        );
            });

    final Function<Flux<String>, Flux<Integer>> integerToStringFluxFilterTransformation = value -> value
            .flatMap(v -> {
                var arrayOfStrings = v.split("");
                return Flux
                        .fromIterable(Arrays
                                .stream(arrayOfStrings)
                                .map(Integer::parseInt)
                                .collect(Collectors.collectingAndThen(Collectors.toList(), c -> !c.isEmpty() ? c : new ArrayList<Integer>()))
                        )
                        .filter(number -> number.equals(0));
            });

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

        var flux01 = Flux.just(data01);
        var flux02 = Flux.just(data02);

        Flux<String> stringFlux = flux01.concatWith(flux02).log();

        stringFlux.subscribe(value -> result.add(value));

        return result;
    }

    public List<String> createConcatenateMono(String data01, String data02) {
        List<String> result = new ArrayList<String>();

        var mono01 = Mono.just(data01);
        var mono02 = Mono.just(data02);

        Flux<String> stringFlux = mono01.concatWith(mono02).log();

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

    public Flux<Integer> createFluxUsingTransform(String data) {
        Flux<Integer> integerFlux = Flux.just(data)
                .transform(integerToStringFluxTransformation)
                .log();
        return integerFlux;
    }

    public Flux<String> namesFlux_transform_switchIfEmpty(List<String> namesList, int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(v -> {
                    var charArray = v.split("");
                    return Flux.fromArray(charArray);
                });

        var defaultFlux = Flux.just("default")
                .transform(filterMap); //"D","E","F","A","U","L","T"

        return Flux.fromIterable(namesList)
                .transform(filterMap) // gives u the opportunity to combine multiple operations using a single call.
                .switchIfEmpty(defaultFlux);
        //using "map" would give the return type as Flux<Flux<String>

    }

    public Flux<String> namesFlux_transform_defaultIfEmpty(List<String> namesList, int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(v -> {
                    var charArray = v.split("");
                    return Flux.fromArray(charArray);
                });
        return Flux.fromIterable(namesList)
                .transform(filterMap)
                .defaultIfEmpty("DEFAULT");
    }

    public Mono<String> namesMono_map_filter_switchIfEmpty(String namesList, int stringLength) {

        Function<Mono<String>, Mono<String>> filterMap = name -> name
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);

        var defaultMono = Mono.just("default")
                .transform(filterMap); //"D","E","F","A","U","L","T"

        return Mono.just(namesList)
                .transform(filterMap)
                .switchIfEmpty(defaultMono);
    }

    public Mono<String> namesMono_transform_defaultIfEmpty(String name, int stringLength) {

        Function<Mono<String>, Mono<String>> filterMap = v -> v.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);
        return Mono.just(name)
                .transform(filterMap)
                .defaultIfEmpty("DEFAULT");
    }
}
