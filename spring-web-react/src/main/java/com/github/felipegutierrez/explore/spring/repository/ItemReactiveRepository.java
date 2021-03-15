package com.github.felipegutierrez.explore.spring.repository;

import com.github.felipegutierrez.explore.spring.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

    Mono<Item> findByDescription(String description);

}
