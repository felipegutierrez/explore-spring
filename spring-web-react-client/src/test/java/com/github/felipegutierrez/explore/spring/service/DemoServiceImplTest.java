package com.github.felipegutierrez.explore.spring.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DemoServiceImplTest {

    protected DemoServiceImpl demoService;

    @BeforeAll
    public void initializeTest() {
        demoService = new DemoServiceImpl();
    }

    @Test
    void sendRequestFor() {
        Mono<String> stringMono = demoService.sendRequestFor();
        stringMono.subscribe();
    }
}