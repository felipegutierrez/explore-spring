package com.github.felipegutierrez.explore.spring.basics.services;

import com.github.felipegutierrez.explore.spring.ExploreSpringApplication;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SomeCdiBusinessTest {

    @Test
    void test() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(ExploreSpringApplication.class)) {
            SomeCdiBusiness someCdiBusiness = applicationContext.getBean(SomeCdiBusiness.class);

            String result = someCdiBusiness.getHostname();
            String expected = "http://127.0.0.1:8080";

            assertEquals(expected, result);
        }
    }
}
