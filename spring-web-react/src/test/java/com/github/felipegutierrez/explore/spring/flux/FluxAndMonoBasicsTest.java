package com.github.felipegutierrez.explore.spring.flux;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FluxAndMonoBasicsTest {
    FluxAndMonoBasics myFluxTest = new FluxAndMonoBasics();

    @Test
    void testCreateBasicFlux() {
        String expect = "Spring,Spring Boot,Reactive Spring";
        String delimiter = ",";

        List<String> result = myFluxTest.createBasicFlux(expect.split(delimiter));

        String actual = result.stream().collect(Collectors.joining(delimiter));

        assertEquals(expect, actual);
    }

    @Test
    void testCreateConcatenateFlux() {
        String expect01 = "Spring,Spring Boot,Reactive Spring";
        String expect02 = "Flux can concatenate,Java is also functional";
        String delimiter = ",";
        String expect = expect01 + delimiter + expect02;

        List<String> result = myFluxTest.createConcatenateFlux(expect01.split(delimiter), expect02.split(delimiter));

        String actual = result.stream().collect(Collectors.joining(delimiter));

        assertEquals(expect, actual);
    }

    @Test
    void testCreateFluxWithComplete() {
        String message = "Spring,Spring Boot,Reactive Spring";
        String completeMsg = "Flux has completed";
        String delimiter = ",";
        String expect = message + completeMsg;


        List<String> result = myFluxTest.createFluxWithComplete(expect.split(delimiter), completeMsg);

        String actual = result.stream().collect(Collectors.joining(delimiter));

        assertEquals(expect, actual);
    }

    @Test
    void testCreateFluxConverter() {
        String expect = "1,23,3456,98,127";
        String delimiter = ",";

        List<Integer> result = myFluxTest.createFluxConverterStringToInt(expect.split(delimiter));

        String actual = result.stream()
                .map(value -> value.toString())
                .collect(Collectors.joining(delimiter));

        assertEquals(expect, actual);
    }

    @Test
    void testCreateFluxConverterWithException() {
        String inputDataWithTypo = "1,23,3456,98a,127";
        String expect = "1,23,3456";
        String delimiter = ",";

        List<Integer> result = myFluxTest.createFluxConverterStringToInt(inputDataWithTypo.split(delimiter));

        String actual = result.stream()
                .map(value -> value.toString())
                .collect(Collectors.joining(delimiter));

        assertEquals(expect, actual);
    }

}
