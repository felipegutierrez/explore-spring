package com.github.felipegutierrez.explore.spring.repository;

import com.github.felipegutierrez.explore.spring.document.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> itemList = Arrays.asList(
            new Item("samtv", "Samsung TV", 400.0),
            new Item(null, "LG TV", 420.0),
            new Item(null, "Sony", 500.0),
            new Item(null, "Google TV", 480.0),
            new Item(null, "Apple watch", 299.99),
            new Item(null, "Beats headphones", 149.99),
            new Item(null, "Piano", 356.50));

    @BeforeAll
    public void setUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("Inserted item: " + item))
                .blockLast();
    }

    @Test
    public void getAllItems() {
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(itemList.size())
                .verifyComplete();
    }

    @Test
    public void getItemById() {
        StepVerifier.create(itemReactiveRepository.findById("samtv"))
                .expectSubscription()
                .expectNextMatches(item -> item.getId().equals("samtv") &&
                        item.getDescription().equals("Samsung TV") &&
                        item.getPrice().equals(Double.valueOf(400.0))
                )
                .verifyComplete();
    }
}
