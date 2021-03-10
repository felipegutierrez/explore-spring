package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.beans.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookController {

    private final List<Book> bookList;

    public BookController() {
        this.bookList = new ArrayList<Book>();
        this.bookList.add(new Book(1L, "The portrait of Dorian Gray", "Oscar Wilde"));
        this.bookList.add(new Book(2L, "To be or not to be", "William Shakespeare"));
        this.bookList.add(new Book(3L, "Sapiens: A Brief History of Humankind", "Yuval Noah Harari"));
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return this.bookList;
    }
}
