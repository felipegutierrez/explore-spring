package com.github.felipegutierrez.explore.spring.repository;

import com.github.felipegutierrez.explore.spring.document.Item;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DirtiesContext
@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> itemList = Arrays.asList(
            new Item("samtv", "Samsung TV", 400.0),
            new Item("lgtv", "LG TV", 420.0),
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
    @Order(1)
    public void getAllItems() {
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(itemList.size())
                .verifyComplete();
    }

    @Test
    @Order(2)
    public void getItemById() {
        StepVerifier.create(itemReactiveRepository.findById("samtv"))
                .expectSubscription()
                .expectNextMatches(item -> item.getId().equals("samtv") &&
                        item.getDescription().equals("Samsung TV") &&
                        item.getPrice().equals(Double.valueOf(400.0))
                )
                .verifyComplete();
    }

    @Test
    @Order(3)
    public void getOneItemByDescription() {
        StepVerifier.create(itemReactiveRepository.findByDescription("Sony").log("getOneItemByDescription"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Order(4)
    public void saveItem() {
        Item item = new Item(null, "piano course", 40.2);
        Mono<Item> savedItemMono = itemReactiveRepository.save(item);
        StepVerifier.create(savedItemMono)
                .expectSubscription()
                .expectNextMatches(i -> i.getId() != null &&
                        i.getDescription().equals("piano course") &&
                        i.getPrice().equals(Double.valueOf(40.2))
                )
                .verifyComplete();
    }

    @Test
    @Order(5)
    public void updateItem() {
        Double newPrice = 459.99;
        Mono<Item> updatedItem = itemReactiveRepository.findByDescription("Sony")
                .map(item -> {
                    item.setPrice(newPrice);
                    return item;
                })
                .flatMap(item -> {
                    return itemReactiveRepository.save(item);
                });

        StepVerifier.create(updatedItem)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice().equals(newPrice))
                .verifyComplete();
    }

    @Test
    @Order(6)
    public void deleteItemById() {
        Mono<Void> deletedItem = itemReactiveRepository.findById("samtv")
                .map(item -> item.getId())
                .flatMap(id -> itemReactiveRepository.deleteById(id));

        StepVerifier.create(deletedItem.log("deleteItemById"))
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll().log("deleteItemById count verify"))
                .expectSubscription()
                .expectNextCount(itemList.size())
                .verifyComplete();
    }

    @Test
    @Order(7)
    public void deleteItem() {
        Mono<Void> deletedItem = itemReactiveRepository.findById("lgtv")
                .flatMap(item -> itemReactiveRepository.delete(item));

        StepVerifier.create(deletedItem.log("deleteItem"))
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll().log("deleteItem count verify"))
                .expectSubscription()
                .expectNextCount(itemList.size() - 1)
                .verifyComplete();
    }

    /*
    @Test
    public void getThreeItemsByDescription() {
        StepVerifier.create(itemReactiveRepository.containsDescription("TV").log("getThreeItemsByDescription"))
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }
    */
}
