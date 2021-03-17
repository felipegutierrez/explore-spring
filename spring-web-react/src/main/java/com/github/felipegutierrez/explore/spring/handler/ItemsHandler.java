package com.github.felipegutierrez.explore.spring.handler;

import com.github.felipegutierrez.explore.spring.document.Item;
import com.github.felipegutierrez.explore.spring.document.ItemCapped;
import com.github.felipegutierrez.explore.spring.repository.ItemCappedReactiveRepository;
import com.github.felipegutierrez.explore.spring.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemsHandler {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Autowired
    ItemCappedReactiveRepository itemCappedReactiveRepository;

    public Mono<ServerResponse> getAllItems(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.findAll(), Item.class);
    }

    public Mono<ServerResponse> getItem(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Item> itemMono = itemReactiveRepository.findById(id);

        return itemMono
                .flatMap(item -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(item)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createItem(ServerRequest serverRequest) {
        Mono<Item> itemMono = serverRequest.bodyToMono(Item.class);
        return itemMono.flatMap(item -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.save(item), Item.class));
    }

    public Mono<ServerResponse> deleteItem(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Void> deletedItemMono = itemReactiveRepository.deleteById(id);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deletedItemMono, Void.class);
    }

    public Mono<ServerResponse> updateItem(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Item> updatedItemMono = serverRequest.bodyToMono(Item.class)
                .flatMap(item -> {
                    Mono<Item> itemMono = itemReactiveRepository.findById(id)
                            .flatMap(currentItem -> {
                                currentItem.setDescription(item.getDescription());
                                currentItem.setPrice(item.getPrice());
                                return itemReactiveRepository.save(currentItem);
                            });
                    return itemMono;
                });
        return updatedItemMono
                .flatMap(item -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(item)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> itemsRuntimeException(ServerRequest serverRequest) {
        throw new RuntimeException("A RuntimeException occurred");
    }

    public Mono<ServerResponse> itemsCappedStream(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(itemCappedReactiveRepository.findItemsBy(), ItemCapped.class);
    }
}
