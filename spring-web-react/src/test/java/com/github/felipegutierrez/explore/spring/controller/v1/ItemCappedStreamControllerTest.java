package com.github.felipegutierrez.explore.spring.controller.v1;

import com.github.felipegutierrez.explore.spring.document.ItemCapped;
import com.github.felipegutierrez.explore.spring.repository.ItemCappedReactiveRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.github.felipegutierrez.explore.spring.util.ItemConstants.ITEM_CAPPED_STREAM_ENDPOINT_V1;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemCappedStreamControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemCappedReactiveRepository itemCappedReactiveRepository;

    @Autowired
    MongoOperations mongoOperations;

    @BeforeAll
    public void setUp() {
        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(ItemCapped.class,
                CollectionOptions.empty().maxDocuments(20).size(50_000).capped());
        Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofMillis(100))
                .map(i -> new ItemCapped(null, "random item capped " + i, 100.0 + i))
                .take(5);
        itemCappedReactiveRepository
                .insert(itemCappedFlux)
                .doOnNext(itemCapped -> System.out.println("Inserted ItemCapped: " + itemCapped))
                .blockLast(); // to wait all 5 itemCapped be inserted
    }

    @Test
    @Order(1)
    public void getStreamItemCapped() {
        Flux<ItemCapped> itemCappedFlux = webTestClient.get().uri(ITEM_CAPPED_STREAM_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ItemCapped.class)
                .getResponseBody()
                .take(5);
        StepVerifier.create(itemCappedFlux)
                .expectSubscription()
                .expectNextCount(5)
                .thenCancel()
                .verify();
    }
}
