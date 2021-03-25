package com.github.felipegutierrez.explore.spring.flux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static reactor.core.scheduler.Schedulers.parallel;

@Slf4j
public class FluxAndMonoTransformations {

    public Flux<String> createFluxUsingFlatMap(List<String> data) {
        return Flux
                .fromIterable(data)
                .flatMap(value -> Flux.fromIterable(simulateDataBaseAccess(value)))
                .log();
    }

    public Flux<String> createFluxUsingFlatMapParallel(List<String> data, Integer windowSize) {
        return Flux
                .fromIterable(data)
                .window(windowSize)
                .flatMap(listValue -> listValue.map(this::simulateDataBaseAccess).subscribeOn(parallel()))
                .flatMap(listValue -> Flux.fromIterable(listValue))
                .log();
    }

    public Flux<String> createFluxUsingFlatMapParallelMaintainOrder(List<String> data, Integer windowSize) {
        return Flux
                .fromIterable(data)
                .window(windowSize)
                .flatMapSequential(listValue -> listValue.map(this::simulateDataBaseAccess).subscribeOn(parallel()))
                .flatMap(listValue -> Flux.fromIterable(listValue))
                .log();
    }

    private List<String> simulateDataBaseAccess(String value) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(value, UUID.randomUUID().toString());
    }

    public Flux<String> createFluxUsingMerge(List<String> list1, List<String> list2) {
        Flux<String> flux1 = Flux.fromIterable(list1);
        Flux<String> flux2 = Flux.fromIterable(list2);
        return Flux.merge(flux1, flux2).log();
    }

    public Flux<String> createFluxUsingMergeWithDelay(List<String> list1, List<String> list2) {
        Flux<String> flux1 = Flux.fromIterable(list1).delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.fromIterable(list2).delayElements(Duration.ofSeconds(1));
        return Flux.merge(flux1, flux2).log();
    }

    public Flux<String> createFluxUsingConcat(List<String> list1, List<String> list2) {
        Flux<String> flux1 = Flux.fromIterable(list1);
        Flux<String> flux2 = Flux.fromIterable(list2);
        return Flux.concat(flux1, flux2).log();
    }

    public Flux<String> createFluxUsingConcatWithDelay(List<String> list1, List<String> list2) {
        Flux<String> flux1 = Flux.fromIterable(list1).delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.fromIterable(list2).delayElements(Duration.ofSeconds(1));
        return Flux.concat(flux1, flux2).log();
    }

    public Flux<String> createFluxUsingZip(List<String> list1, List<String> list2) {
        Flux<String> flux1 = Flux.fromIterable(list1);
        Flux<String> flux2 = Flux.fromIterable(list2);
        return Flux
                .zip(flux1, flux2, (tuple1, tuple2) -> tuple1.concat(tuple2))
                .log();
    }

    public Flux<GroupedFlux<Integer, Data>> createFluxUsingGroupBy(List<String> dataList, int numberOfPartitions, int maxCount) {
        return Flux
                .fromStream(IntStream.range(0, maxCount)
                        .mapToObj(i -> {
                            int randomPosition = ThreadLocalRandom.current().nextInt(0, dataList.size());
                            int partition = i % numberOfPartitions;
                            return new Data(i, dataList.get(randomPosition), partition);
                        })
                )
                .delayElements(Duration.ofMillis(10))
                .log()
                .groupBy(Data::getPartition)
                ;
    }

    public Flux<GroupedFlux<Integer, Data>> createFluxUsingHashGroupBy(List<String> dataList, int numberOfPartitions, int parallelism, int maxCount) {
        return Flux
                .fromStream(IntStream.range(0, maxCount)
                        .mapToObj(i -> {
                            int randomPosition = ThreadLocalRandom.current().nextInt(0, dataList.size());
                            String value = dataList.get(randomPosition);
                            int partition = (getDifferentHashCode(value) * parallelism) % numberOfPartitions;
                            return new Data(i, value, partition);
                        })
                )
                .delayElements(Duration.ofMillis(10))
                .log()
                .groupBy(Data::getPartition)
                ;
    }

    public int getDifferentHashCode(String value) {
        int hash = 7;
        for (int i = 0; i < value.length(); i++) {
            hash = hash * 31 + value.charAt(i);
        }
        return hash;
    }
}
