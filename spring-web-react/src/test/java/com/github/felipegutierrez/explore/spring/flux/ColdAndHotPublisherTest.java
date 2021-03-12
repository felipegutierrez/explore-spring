package com.github.felipegutierrez.explore.spring.flux;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class ColdAndHotPublisherTest {

    ColdAndHotPublisher coldAndHotPublisher = new ColdAndHotPublisher();
    List<String> list = Arrays.asList("Spring", "Spring Boot", "Reactive Spring", "Java 8", "Project reactor", "Scala did it first");
    List<String> expect = Stream.concat(list.stream(), list.stream()).collect(Collectors.toList());

    @Test
    public void testFluxColdPublisher() throws InterruptedException {
        List<String> result = coldAndHotPublisher.coldPublisher(list);

        assertEquals(expect.size(), result.size());
        assertArrayEquals(expect.stream().sorted().toArray(), result.stream().sorted().toArray());
    }

    @Test
    public void testFluxHotPublisher() throws InterruptedException {
        List<String> result = coldAndHotPublisher.hotPublisher(list);

        assertNotEquals(expect.size(), result.size());
    }
}
