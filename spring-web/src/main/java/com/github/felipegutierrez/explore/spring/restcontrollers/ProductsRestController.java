package com.github.felipegutierrez.explore.spring.restcontrollers;

import com.github.felipegutierrez.explore.spring.beans.Product;
import com.github.felipegutierrez.explore.spring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// @Controller
@RestController
public class ProductsRestController {

    @Autowired
    private ProductRepository productRepository;

    // @ResponseBody
    @GetMapping("/hplus/rest/products")
    public List<Product> getProducts() {
        //calll product repo
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(product -> products.add(product));
        return products;

    }

    @GetMapping("/hplus/rest/product")
    public ResponseEntity getProductsByRequestParam(@RequestParam("name") String name){
        List<Product> products = productRepository.searchByName(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/hplus/rest/product/{name}")
    public ResponseEntity getProductsByPathVariable(@PathVariable("name") String name){
        List<Product> products = productRepository.searchByName(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
