package com.github.felipegutierrez.explore.spring.controller.v1;

import com.github.felipegutierrez.explore.spring.document.ItemCapped;
import com.github.felipegutierrez.explore.spring.repository.ItemCappedReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.github.felipegutierrez.explore.spring.util.ItemConstants.ITEM_CAPPED_STREAM_ENDPOINT_V1;

@RestController
public class ItemCappedStreamController {

    @Autowired
    ItemCappedReactiveRepository itemCappedReactiveRepository;

    @GetMapping(value = ITEM_CAPPED_STREAM_ENDPOINT_V1, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ItemCapped> getItemCappedStream() {
        return itemCappedReactiveRepository.findItemsBy();
    }
}
