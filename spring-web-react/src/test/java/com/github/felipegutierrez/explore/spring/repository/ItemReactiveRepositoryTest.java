package com.github.felipegutierrez.explore.spring.repository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Test
    public void getAllItems() {
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();

    }
}
