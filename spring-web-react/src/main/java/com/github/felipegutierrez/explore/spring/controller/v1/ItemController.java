package com.github.felipegutierrez.explore.spring.controller.v1;

import com.github.felipegutierrez.explore.spring.document.Item;
import com.github.felipegutierrez.explore.spring.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.github.felipegutierrez.explore.spring.util.ItemConstants.ENDPOINT_V1_ITEM_GET_ALL;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @GetMapping(ENDPOINT_V1_ITEM_GET_ALL)
    public Flux<Item> getAllItems() {
        return itemReactiveRepository.findAll();
    }
}
