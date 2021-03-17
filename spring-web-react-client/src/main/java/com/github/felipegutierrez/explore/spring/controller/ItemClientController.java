package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.github.felipegutierrez.explore.spring.util.ItemConstants.ITEM_ENDPOINT_V1;

@RestController
@Slf4j
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

    /**
     * using: http PUT http://localhost:8081/client/updateItem/hardcodeID < spring-web-react-client/src/main/resources/sampleItemToUpdate.json
     *
     * @param id
     * @param item
     * @return
     */
    @PutMapping("/client/updateItem" + "/{id}")
    public Mono<Item> updateItem(@PathVariable String id, @RequestBody Item item) {
        Mono<Item> itemMono = Mono.just(item);
        return webClient.put().uri(ITEM_ENDPOINT_V1 + "/{id}", id)
                .body(itemMono, Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("updated item: ");
    }

    /**
     * using: http PUT http://localhost:8081/client/updateItemExchange/hardcodeID < spring-web-react-client/src/main/resources/sampleItemToUpdate.json
     *
     * @param id
     * @param item
     * @return
     */
    @PutMapping("/client/updateItemExchange" + "/{id}")
    public Mono<Item> updateItemExchange(@PathVariable String id, @RequestBody Item item) {
        Mono<Item> itemMono = Mono.just(item);
        return webClient.put().uri(ITEM_ENDPOINT_V1 + "/{id}", id)
                .body(itemMono, Item.class)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("updated item exchange: ");
    }

    /**
     * using: http DELETE http://localhost:8081/client/deleteItem/hardcodeID
     *
     * @param id
     * @return
     */
    @DeleteMapping("/client/deleteItem" + "/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {
        return webClient.delete().uri(ITEM_ENDPOINT_V1 + "/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .log("deleted item: ");
    }

    /**
     * using: http DELETE http://localhost:8081/client/deleteItemExchange/hardcodeID
     *
     * @param id
     * @return
     */
    @DeleteMapping("/client/deleteItemExchange" + "/{id}")
    public Mono<Void> deleteItemExchange(@PathVariable String id) {
        return webClient.delete().uri(ITEM_ENDPOINT_V1 + "/{id}", id)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Void.class))
                .log("deleted item: ");
    }

    @GetMapping("/client/retrieve/error")
    public Flux<Item> errorRetrieve() {
        return webClient.get().uri("/v1/func/runtimeexception")
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    Mono<String> errorMono = clientResponse.bodyToMono(String.class);
                    return errorMono.flatMap(errorMsg -> {
                        log.error("The error message is: {}", errorMsg);
                        return Mono.error(new RuntimeException(errorMsg));
                    });
                })
                .bodyToFlux(Item.class);
    }

    @GetMapping("/client/exchange/error")
    public Flux<Item> errorExchange() {
        return webClient.get().uri("/v1/func/runtimeexception")
                .exchangeToFlux(clientResponse -> {
                    if (clientResponse.statusCode().is5xxServerError()) {
                        return clientResponse.bodyToFlux(String.class)
                                .flatMap(errorMsg -> {
                                    log.error("Error message on errorExchange: {}", errorMsg);
                                    return Mono.error(new RuntimeException(errorMsg));
                                });
                    } else {
                        return clientResponse.bodyToFlux(Item.class);
                    }
                });
    }
}
