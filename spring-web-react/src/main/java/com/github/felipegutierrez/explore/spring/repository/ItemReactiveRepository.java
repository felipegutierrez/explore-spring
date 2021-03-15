package com.github.felipegutierrez.explore.spring.repository;

import com.github.felipegutierrez.explore.spring.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

}
