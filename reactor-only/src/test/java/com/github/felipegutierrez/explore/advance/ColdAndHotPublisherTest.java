package com.github.felipegutierrez.explore.advance;

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
    public void testFluxColdPublisher() {
        List<String> result = coldAndHotPublisher.coldPublisher(list);

        assertEquals(expect.size(), result.size());
        assertArrayEquals(expect.stream().sorted().toArray(), result.stream().sorted().toArray());
    }

    @Test
    public void testFluxHotPublisher() {
        List<String> result = coldAndHotPublisher.hotPublisher(list);

        assertNotEquals(list.size(), result.size());
    }

    @Test
    public void hotPublisherAuto() {
        List<String> result = coldAndHotPublisher.hotPublisherAuto(list);

        assertNotEquals(list.size(), result.size());
    }

    @Test
    void hotPublisherRefCount() {
        List<String> result = coldAndHotPublisher.hotPublisherRefCount(list);

        assertEquals(expect.size(), result.size());
    }
}
