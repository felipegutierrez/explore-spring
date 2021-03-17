package com.github.felipegutierrez.explore.spring.controller.v1;

import com.github.felipegutierrez.explore.spring.document.Item;
import com.github.felipegutierrez.explore.spring.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.github.felipegutierrez.explore.spring.util.ItemConstants.ITEM_ENDPOINT_V1;
import static com.github.felipegutierrez.explore.spring.util.ItemConstants.RUNTIME_EXCEPTION_MSG;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    // moved to the global handler
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
//        log.error("Exception caught in handleRuntimeException: {}", ex);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ex.getMessage());
//    }

    @GetMapping(ITEM_ENDPOINT_V1)
    public Flux<Item> getAllItems() {
        return itemReactiveRepository.findAll();
    }

    @GetMapping(ITEM_ENDPOINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> getItem(@PathVariable String id) {
        return itemReactiveRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ITEM_ENDPOINT_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return itemReactiveRepository.save(item);
    }

    @DeleteMapping(ITEM_ENDPOINT_V1 + "/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {
        return itemReactiveRepository.deleteById(id);
    }

    /**
     * 1 - pass the ID and the Item to be updated
     * 2 - retrieve the Item from MongoDB
     * 3 - update the Item from MongoDB with the new price and Description
     * 4 - update the Item on MongoDB
     * 5 - return the saved Item
     *
     * @param id
     * @param item
     * @return
     */
    @PutMapping(ITEM_ENDPOINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id, @RequestBody Item item) {
        return itemReactiveRepository.findById(id)
                .flatMap(currentItem -> {
                    currentItem.setDescription(item.getDescription());
                    currentItem.setPrice(item.getPrice());
                    return itemReactiveRepository.save(currentItem);
                })
                .map(updatedItem -> new ResponseEntity<>(updatedItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * using http://localhost:8080/v1/item/runtimeException
     *
     * @return
     */
    @GetMapping(ITEM_ENDPOINT_V1 + "/runtimeException")
    public Flux<Item> runtimeException() {
        return itemReactiveRepository
                .findAll()
                .concatWith(Mono.error(new RuntimeException(RUNTIME_EXCEPTION_MSG)));
    }

    /**
     * using http://localhost:8080/v1/item/exception
     *
     * @return
     */
    @GetMapping(ITEM_ENDPOINT_V1 + "/exception")
    public Flux<Item> exception() {
        return itemReactiveRepository
                .findAll()
                .concatWith(Mono.error(new NumberFormatException("A NumberFormatException occurred in the Item end-point. Please contact the system administrator.")));
    }
}
