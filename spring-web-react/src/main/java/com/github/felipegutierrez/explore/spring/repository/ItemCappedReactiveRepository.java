package com.github.felipegutierrez.explore.spring.repository;

import com.github.felipegutierrez.explore.spring.document.ItemCapped;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ItemCappedReactiveRepository extends ReactiveMongoRepository<ItemCapped, String>, CustomItemReactiveRepository {

    @Tailable
    Flux<ItemCapped> findItemsBy();
}
