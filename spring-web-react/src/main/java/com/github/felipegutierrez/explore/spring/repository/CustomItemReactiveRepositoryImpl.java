package com.github.felipegutierrez.explore.spring.repository;

import com.github.felipegutierrez.explore.spring.document.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

/**
 * Implemented based on https://dev.to/iuriimednikov/how-to-build-custom-queries-with-spring-data-reactive-mongodb-1802
 */
public class CustomItemReactiveRepositoryImpl implements CustomItemReactiveRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public CustomItemReactiveRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Item> findItemByPriceLowerThan(Double maxPrice) {
        // step 1 - Create a query object that finds
        Query query = new Query(Criteria.where("price").lte(maxPrice));
        // step 2 - use the mongo template
        return mongoTemplate.find(query, Item.class);
    }
}
