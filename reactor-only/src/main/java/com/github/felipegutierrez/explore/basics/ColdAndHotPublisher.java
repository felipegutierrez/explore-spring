package com.github.felipegutierrez.explore.basics;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ColdAndHotPublisher {

    public static void main(String[] args) throws InterruptedException {
        List<String> list = Arrays.asList("Spring", "Spring Boot", "Reactive Spring", "Java 8", "Project reactor", "Scala did it first");

        ColdAndHotPublisher coldAndHotPublisher = new ColdAndHotPublisher();
        coldAndHotPublisher.coldPublisher(list);
        coldAndHotPublisher.hotPublisher(list);
    }

    public List<String> coldPublisher(List<String> list) throws InterruptedException {
        List<String> result = new ArrayList<String>();
        log.info("Cold subscriber");
        Flux<String> stringFlux = Flux.fromIterable(list)
                .delayElements(Duration.ofSeconds(1));
        stringFlux.subscribe(v -> {
                    log.info("Sub 1: " + v);
                    result.add(v);
                }
        );
        Thread.sleep(3000);
        stringFlux.subscribe(v -> {
            log.info("Sub 2: " + v);
            result.add(v);
        });
        Thread.sleep(7000);
        return result;
    }

    public List<String> hotPublisher(List<String> list) throws InterruptedException {
        List<String> result = new ArrayList<String>();
        log.info("Hot subscriber");
        Flux<String> stringFlux = Flux.fromIterable(list)
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.connect();
        connectableFlux.subscribe(v -> {
            log.info("Sub 1: " + v);
            result.add(v);
        });
        Thread.sleep(3000);
        connectableFlux.subscribe(v -> {
            log.info("Sub 2: " + v);
            result.add(v);
        });
        Thread.sleep(5000);
        return result;
    }
}
