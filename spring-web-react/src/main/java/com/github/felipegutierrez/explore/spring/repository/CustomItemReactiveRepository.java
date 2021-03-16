package com.github.felipegutierrez.explore.spring.repository;

import com.github.felipegutierrez.explore.spring.document.Item;
import reactor.core.publisher.Flux;

public interface CustomItemReactiveRepository {
    Flux<Item> findItemByPriceLowerThan(Double maxPrice);
}
