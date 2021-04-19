package com.github.felipegutierrez.explore.basics;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
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

    @Test
    void testCreateBasicFluxWithoutSubscribe() {
        String expect = "Spring,Spring Boot,Reactive Spring";
        String delimiter = ",";
        String[] messages = expect.split(delimiter);

        Flux<String> stringFlux = myFluxTest.createBasicFluxWithoutSubscribe(messages);

        // the StepVerifier.create calls the subscribe for us
        StepVerifier.create(stringFlux)
                .expectNext(messages[0])
                .expectNext(messages[1])
                .expectNext(messages[2])
                .verifyComplete();

        StepVerifier.create(stringFlux)
                .expectNext(messages)
                .verifyComplete();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void testCreateFluxConverterHandleException() {
        String inputDataWithTypo = "1,23,3456,98a,127";
        String delimiter = ",";
        String[] messages = inputDataWithTypo.split(delimiter);
        Integer[] expectedMessages = new Integer[]{1, 23, 3456};

        Flux<Integer> integerFlux = myFluxTest.createFluxWithoutSubscribeConverterStringToInt(messages);
        StepVerifier.create(integerFlux)
                .expectNext(Integer.valueOf(messages[0]))
                .expectNext(Integer.valueOf(messages[1]))
                .expectNext(Integer.valueOf(messages[2]))
                .expectError(NumberFormatException.class)
                .verify();

        StepVerifier.create(integerFlux)
                .expectNext(expectedMessages)
                .expectError(NumberFormatException.class)
                .verify();

        StepVerifier.create(integerFlux)
                .expectNextCount(3)
                .expectError(NumberFormatException.class)
                .verify();
    }

    @Test
    void testCreateBasicMono() {
        String expect = "1258";

        Mono<Integer> integerMono = myFluxTest.createMonoConverterStringToInt(expect);

        StepVerifier.create(integerMono)
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(integerMono)
                .expectNext(Integer.valueOf(expect))
                .verifyComplete();
    }

    @Test
    void testCreateBasicMonoWithError() {
        String expect = "Spring Boot Reactive";

        Mono<Integer> integerMono = myFluxTest.createMonoConverterStringToInt(expect);

        StepVerifier.create(integerMono)
                .expectError(NumberFormatException.class)
                .verify();

        StepVerifier.create(integerMono)
                .expectNextCount(0)
                .expectError(NumberFormatException.class)
                .verify();
    }

    @Test
    void createBasicFluxWithFlatmap() {
        String actual = "reactor-project";
        String delimiter = "";
        String[] expected = actual.split(delimiter);
        Flux<String> stringFlux = myFluxTest.createBasicFluxWithFlatmap(List.of(actual));

        // the StepVerifier.create calls the subscribe for us
        StepVerifier.create(stringFlux)
                .expectNext(expected[0])
                .expectNext(expected[1])
                .expectNext(expected[2])
                .expectNext(expected[3])
                .expectNext(expected[4])
                .expectNext(expected[5])
                .expectNext(expected[6])
                .expectNext(expected[7])
                .expectNext(expected[8])
                .expectNext(expected[9])
                .expectNext(expected[10])
                .expectNext(expected[11])
                .expectNext(expected[12])
                .expectNext(expected[13])
                .expectNext(expected[14])
                .verifyComplete();

        StepVerifier.create(stringFlux)
                .expectNext(expected)
                .verifyComplete();

        StepVerifier.create(stringFlux)
                .expectNextCount(15)
                .verifyComplete();
    }

    @Test
    void createBasicFluxWithFlatmapDelay() {
        var actual = "reactorprojectinjava8";
        var delimiter = "";
        String[] expected = actual.split(delimiter);
        Flux<String> stringFlux = myFluxTest.createBasicFluxWithFlatmapDelay(List.of("reactor", "project", "in", "java", "8"));

        /** flatmap does not guarantee order, hence this test will not pass eventually */
        //        StepVerifier.create(stringFlux)
        //                .expectNext(expected)
        //                .verifyComplete();

        StepVerifier.create(stringFlux)
                .expectNextCount(21)
                .verifyComplete();
    }

    @Test
    void createBasicFluxWithConcatmapDelay() {

        var actual = "reactorprojectinjava8";
        var delimiter = "";
        String[] expected = actual.split(delimiter);
        Flux<String> stringFlux = myFluxTest.createBasicFluxWithConcatmapDelay(List.of("reactor", "project", "in", "java", "8"));

        /** concatMap guarantees order, hence this test will not pass eventually */
        StepVerifier.create(stringFlux)
                .expectNext(expected)
                .verifyComplete();

        StepVerifier.create(stringFlux)
                .expectNextCount(21)
                .verifyComplete();
    }

    @Test
    void createFluxFromMono() {
        var actual = "1258";
        var expected = Arrays.stream(actual.split(""))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Flux<Integer> integerFlux = myFluxTest.createFluxFromMono(actual);

        StepVerifier.create(integerFlux)
                .expectNextCount(4)
                .verifyComplete();

        StepVerifier.create(integerFlux)
                .expectNext(expected.get(0))
                .expectNext(expected.get(1))
                .expectNext(expected.get(2))
                .expectNext(expected.get(3))
                .verifyComplete();
    }

    @Test
    void createFluxUsingTransform() {
        var actual = "1258";
        var expected = Arrays.stream(actual.split(""))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Flux<Integer> integerFlux = myFluxTest.createFluxUsingTransform(actual);

        StepVerifier.create(integerFlux)
                .expectNextCount(4)
                .verifyComplete();

        StepVerifier.create(integerFlux)
                .expectNext(expected.get(0))
                .expectNext(expected.get(1))
                .expectNext(expected.get(2))
                .expectNext(expected.get(3))
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_switchIfEmpty() {

        //given
        var namesList = List.of("alex", "ben", "chloe");
        int stringLength = 6;

        //when
        var namesFlux = myFluxTest.namesFlux_transform_switchIfEmpty(namesList, stringLength)
                .log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();
    }

    @Test
    void namesMono_map_filter_switchIfEmpty() {
        //given
        var name = "alex";
        int stringLength = 6;

        //when
        var namesFlux = myFluxTest.namesMono_map_filter_switchIfEmpty(name, stringLength)
                .log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("DEFAULT")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_defaultIfEmpty() {
        //given
        var namesList = List.of("alex", "ben", "chloe");
        int stringLength = 6;

        //when
        var namesFlux = myFluxTest.namesFlux_transform_defaultIfEmpty(namesList, stringLength)
                .log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("DEFAULT")
                .verifyComplete();
    }

    @Test
    void namesMono_transform_defaultIfEmpty() {
        //given
        var name = "alex";
        int stringLength = 6;

        //when
        var namesFlux = myFluxTest.namesMono_transform_defaultIfEmpty(name, stringLength)
                .log();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("DEFAULT")
                .verifyComplete();
    }
}
