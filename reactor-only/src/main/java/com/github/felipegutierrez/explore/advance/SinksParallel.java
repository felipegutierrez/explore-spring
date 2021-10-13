package com.github.felipegutierrez.explore.advance;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * https://stackoverflow.com/questions/68542082/reactor-groupby-with-parallelism-runs-on-same-thread
 */
@Slf4j
public class SinksParallel {

    private final AtomicInteger counter = new AtomicInteger(1);
    private Sinks.Many<Integer> healthSink;
    private Disposable dispose;

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        SinksParallel sinksParallel = new SinksParallel();

    }

    public Flux<Integer> start() {
        healthSink = Sinks.many()
                // .unicast()
                .multicast()
                .onBackpressureBuffer()
        ;
        return
                // healthSink.asFlux()
                Flux.fromStream(IntStream.range(0, 20).mapToObj(Integer::valueOf))
                        .delayElements(Duration.ofMillis(10))
                        .log("source")
                        .groupBy(v -> v % 3 == 0)
                        // .parallel(10)
                        // .runOn(Schedulers.parallel())
                        // .runOn(Schedulers.fromExecutor(executor))
                        // .runOn(Schedulers.newBoundedElastic(10, 100, "k-task"))
                        .flatMap(v -> v)
                        .log("flatmap")
                        // .subscribe(v -> log.info("Data {}", v))
        ;
    }

    public void stop() {
        // executor.shutdownNow();
        if (dispose != null) {
            dispose.dispose();
        }
    }
}
