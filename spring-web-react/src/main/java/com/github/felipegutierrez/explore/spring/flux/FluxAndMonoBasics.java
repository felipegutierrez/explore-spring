package com.github.felipegutierrez.explore.spring.flux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

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

        Flux<String> stringFlux = Flux.just(data);

        stringFlux.subscribe(value -> result.add(value));

        return result;
    }

    public List<String> createConcatenateFlux(String[] data01, String[] data02) {
        List<String> result = new ArrayList<String>();

        Flux<String> stringFlux = Flux
                .just(data01)
                .concatWith(Flux.just(data02));

        stringFlux.subscribe(value -> result.add(value));

        return result;
    }

    public List<String> createFluxWithComplete(String[] data, String completeMsg) {
        List<String> result = new ArrayList<String>();

        Flux<String> stringFlux = Flux.just(data);

        stringFlux.subscribe(value -> result.add(value),
                throwable -> System.out.println("Exception is: " + throwable),
                () -> System.out.println(completeMsg));

        return result;
    }

    public List<Integer> createFluxConverterStringToInt(String[] data) {
        try {
            List<Integer> result = new ArrayList<Integer>();

            Flux<Integer> stringFlux = Flux
                    .just(data)
                    .map(value -> Integer.parseInt(value));

            stringFlux.subscribe(value -> result.add(value));

            return result;
        } catch (Exception ex) {
            log.error("Exception at FluxAndMonoBasics.createFluxConverterStringToInt: {}", ex);
            throw ex;
        }
    }
}
