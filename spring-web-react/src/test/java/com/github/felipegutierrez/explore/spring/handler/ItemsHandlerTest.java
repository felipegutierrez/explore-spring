package com.github.felipegutierrez.explore.spring.handler;

import com.github.felipegutierrez.explore.spring.document.Item;
import com.github.felipegutierrez.explore.spring.repository.ItemReactiveRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static com.github.felipegutierrez.explore.spring.util.ItemConstants.ITEM_FUNCTIONAL_ENDPOINT_V1;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemsHandlerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public List<Item> data() {
        return Arrays.asList(
                new Item(null, "Samsung TV", 399.99),
                new Item(null, "LG TV", 369.99),
                new Item("gTvId", "Google TV", 439.99),
                new Item("hardcodeID", "Sony TV", 424.99));
    }

    @BeforeAll
    public void setUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("inserted item: " + item))
                .blockLast();
    }

    @Test
    @Order(1)
    public void getAllItemsApproach01() {
        webTestClient.get().uri(ITEM_FUNCTIONAL_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(data().size());
    }

    @Test
    @Order(2)
    public void getAllItemsApproach02() {
        webTestClient.get().uri(ITEM_FUNCTIONAL_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(data().size())
                .consumeWith(response -> {
                    List<Item> items = response.getResponseBody();
                    items.forEach(item -> assertTrue(item.getId() != null));
                });
    }

    @Test
    @Order(3)
    public void getAllItemsApproach03() {
        Flux<Item> itemsFlux = webTestClient.get().uri(ITEM_FUNCTIONAL_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class)
                .getResponseBody();
        StepVerifier.create(itemsFlux.log("getAllItemsApproach03"))
                .expectSubscription()
                .expectNextCount(data().size())
                .verifyComplete();
    }

    @Test
    @Order(4)
    public void getItem() {
        webTestClient.get().uri(ITEM_FUNCTIONAL_ENDPOINT_V1.concat("/{id}"), "hardcodeID")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 424.99);
    }

    @Test
    @Order(5)
    public void getItemNotFound() {
        webTestClient.get().uri(ITEM_FUNCTIONAL_ENDPOINT_V1.concat("/{id}"), "unknownID")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(6)
    public void createItem() {
        Item newItem = new Item(null, "piano course", 49.99);
        webTestClient.post().uri(ITEM_FUNCTIONAL_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newItem), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("piano course")
                .jsonPath("$.price").isEqualTo(49.99);
    }

    @Test
    @Order(7)
    public void deleteItem() {
        webTestClient.delete().uri(ITEM_FUNCTIONAL_ENDPOINT_V1.concat("/{id}"), "hardcodeID")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    @Order(8)
    public void updateItem() {
        String id = "gTvId";
        Double newPrice = 479.99;
        String newDesc = "Google TV new generation";
        Item newItem = new Item(null, newDesc, newPrice);
        webTestClient.put().uri(ITEM_FUNCTIONAL_ENDPOINT_V1.concat("/{id}"), id, newItem)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newItem), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.description").isEqualTo(newDesc)
                .jsonPath("$.price").isEqualTo(479.99);
    }

    @Test
    @Order(9)
    public void updateItemWithInvalidId() {
        String id = "unknown";
        Double newPrice = 479.99;
        String newDesc = "Google TV new generation";
        Item newItem = new Item(null, newDesc, newPrice);
        webTestClient.put().uri(ITEM_FUNCTIONAL_ENDPOINT_V1.concat("/{id}"), id, newItem)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newItem), Item.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}
