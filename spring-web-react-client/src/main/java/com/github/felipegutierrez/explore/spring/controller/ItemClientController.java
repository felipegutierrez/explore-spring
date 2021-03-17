package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.domain.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.github.felipegutierrez.explore.spring.util.ItemConstants.ITEM_ENDPOINT_V1;

@RestController
public class ItemClientController {

    WebClient webClient = WebClient.create("http://localhost:8080");

    /**
     * using: http GET http://localhost:8081/client/retrieve
     *
     * @return
     */
    @GetMapping("/client/retrieve")
    public Flux<Item> getAllItemsUsingRetrieve() {
        return webClient.get().uri(ITEM_ENDPOINT_V1)
                .retrieve()
                .bodyToFlux(Item.class)
                .log("Items in client project retrieve: ");
    }

    /**
     * using: http GET http://localhost:8081/client/exchange
     *
     * @return
     */
    @GetMapping("/client/exchange")
    public Flux<Item> getAllItemsUsingExchange() {
        return webClient.get().uri(ITEM_ENDPOINT_V1)
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Item.class))
                .log("Items in client project exchange: ");
    }

    /**
     * using: http GET http://localhost:8081/client/retrieve/singleItem
     *
     * @return
     */
    @GetMapping("/client/retrieve/singleItem")
    public Mono<Item> getItemUsingRetrieve() {
        String id = "hardcodeID";
        return webClient.get().uri(ITEM_ENDPOINT_V1 + "/{id}", id)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Item in client project retrieve: ");
    }

    /**
     * using: http GET http://localhost:8081/client/exchange/singleItem
     *
     * @return
     */
    @GetMapping("/client/exchange/singleItem")
    public Mono<Item> getItemUsingExchange() {
        String id = "hardcodeID";
        return webClient.get().uri(ITEM_ENDPOINT_V1 + "/{id}", id)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("Item in client project exchange: ");
    }

    /**
     * using: http GET http://localhost:8081/client/retrieve/hardcodeID
     *
     * @return
     */
    @GetMapping("/client/retrieve" + "/{id}")
    public Mono<Item> getItemUsingRetrieve(@PathVariable String id) {
        return webClient.get().uri(ITEM_ENDPOINT_V1 + "/{id}", id)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Item in client project retrieve: ");
    }

    /**
     * using: http GET http://localhost:8081/client/exchange/hardcodeID
     *
     * @return
     */
    @GetMapping("/client/exchange" + "/{id}")
    public Mono<Item> getItemUsingExchange(@PathVariable String id) {
        return webClient.get().uri(ITEM_ENDPOINT_V1 + "/{id}", id)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("Item in client project exchange: ");
    }

    /**
     * using: http POST http://localhost:8081/client/createItem < spring-web-react-client/src/main/resources/sampleItem.json
     *
     * @param item
     * @return
     */
    @PostMapping("/client/createItem")
    public Mono<Item> createItem(@RequestBody Item item) {
        Mono<Item> itemMono = Mono.just(item);
        return webClient.post().uri(ITEM_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemMono, Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("created item: ");
    }

    /**
     * using: http POST http://localhost:8081/client/createItemExchange < spring-web-react-client/src/main/resources/sampleItem.json
     *
     * @param item
     * @return
     */
    @PostMapping("/client/createItemExchange")
    public Mono<Item> createItemExchange(@RequestBody Item item) {
        Mono<Item> itemMono = Mono.just(item);
        return webClient.post().uri(ITEM_ENDPOINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemMono, Item.class)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("created item: ");
    }
}
