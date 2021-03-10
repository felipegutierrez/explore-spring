package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.beans.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Book> getAllBooks(@RequestParam Optional<List<Long>> ids) {
        List<Long> listOfIds = ids.orElse(new ArrayList<Long>());
        if (listOfIds.isEmpty()) {
            // get all books
            return this.bookList;
        } else {
            // get only books with IDs present on the list listOfIds
            return this.bookList.stream()
                    .filter(book -> listOfIds.contains(book.getId()))
                    .collect(Collectors.toList());
        }
    }

    @GetMapping("/book")
    public Optional<Book> getBook(@RequestParam Long id) {
        Optional<Book> result = this.bookList.stream()
                .filter(book -> book.getId().equals(id))
                .collect(Collectors.reducing((a, b) -> null));
        return result;
    }

    @GetMapping("/book/searchByName")
    public List<Book> getBooksByName(@RequestParam String name) {
        List<Book> result = this.bookList.stream()
                .filter(book -> book.getName().contains(name))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/book/searchByAuthor")
    public List<Book> getBooksByAuthor(@RequestParam String author) {
        List<Book> result = this.bookList.stream()
                .filter(book -> book.getAuthor().contains(author))
                .collect(Collectors.toList());
        return result;
    }
}
