package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class BookClientController {

    @Autowired
    private BookService bookService;

    @RequestMapping("/to-read")
    public Mono<String> toRead() {
        return bookService.readingList();
    }
}
