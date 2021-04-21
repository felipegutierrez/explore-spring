package com.github.felipegutierrez.explore.advance;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.felipegutierrez.explore.util.CommonUtil.delay;

@Slf4j
public class ColdAndHotPublisher {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("Spring", "Spring Boot", "Reactive Spring", "Java 8", "Project reactor", "Scala did it first");

        ColdAndHotPublisher coldAndHotPublisher = new ColdAndHotPublisher();
        coldAndHotPublisher.coldPublisher(list);
        coldAndHotPublisher.hotPublisher(list);
    }

    public List<String> coldPublisher(List<String> list) {
        List<String> result = new ArrayList<String>();
        log.info("Cold subscriber");
        Flux<String> stringFlux = Flux.fromIterable(list)
                .delayElements(Duration.ofSeconds(1));
        stringFlux.subscribe(v -> {
                    log.info("Sub 1: " + v);
                    result.add(v);
                }
        );
        delay(3000);
        stringFlux.subscribe(v -> {
            log.info("Sub 2: " + v);
            result.add(v);
        });
        delay(7000);
        return result;
    }

    public List<String> hotPublisher(List<String> list) {
        List<String> result = new ArrayList<String>();
        log.info("Hot subscriber");
        Flux<String> stringFlux = Flux.fromIterable(list)
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.connect();
        connectableFlux.subscribe(v -> {
            log.info("Sub 1: " + v);
        });
        delay(3000);
        connectableFlux.subscribe(v -> {
            log.info("Sub 2: " + v);
            result.add(v);
        });
        delay(5000);
        return result;
    }

    public List<String> hotPublisherAuto(List<String> list) {
        List<String> result = new ArrayList<>();
        log.info("Hot publisher auto");
        Flux<String> stringFlux = Flux.fromIterable(list)
                .delayElements(Duration.ofSeconds(1));

        var hotAutoConnectFlux = stringFlux.publish().autoConnect(2);

        hotAutoConnectFlux.subscribe(v -> {
            log.info("Sub 1: " + v);
        });
        delay(500);
        hotAutoConnectFlux.subscribe(v -> {
            log.info("Sub 2: " + v);
        });
        delay(2000);
        hotAutoConnectFlux.subscribe(v -> {
            log.info("Sub 3: " + v);
            result.add(v);
        });
        delay(7000);

        return result;
    }

    public List<String> hotPublisherRefCount(List<String> list) {
        List<String> result = new ArrayList<>();
        log.info("Hot publisher refCount");
        Flux<String> stringFlux = Flux.fromIterable(list)
                .delayElements(Duration.ofSeconds(1));

        var hotRefCountFlux = stringFlux.publish().refCount(2);

        var disposableFlux1 = hotRefCountFlux.subscribe(v -> {
            log.info("Sub 1: " + v);
        });
        delay(500);
        var disposableFlux2 = hotRefCountFlux.subscribe(v -> {
            log.info("Sub 2: " + v);
        });
        delay(2000);

        // throw away the Flux's that are already subscribed
        disposableFlux1.dispose();
        disposableFlux2.dispose();

        // subscribe other Flux's
        hotRefCountFlux.subscribe(v -> {
            log.info("Sub 3: " + v);
            result.add(v);
        });
        hotRefCountFlux.subscribe(v -> {
            log.info("Sub 4: " + v);
            result.add(v);
        });
        delay(7000);

        return result;
    }
}
