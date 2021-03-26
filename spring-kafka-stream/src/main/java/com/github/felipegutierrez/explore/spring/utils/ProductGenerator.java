package com.github.felipegutierrez.explore.spring.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.felipegutierrez.explore.spring.model.LineItem;
import com.github.felipegutierrez.explore.spring.model.LineItemAvro;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.Random;

@Service
class ProductGenerator {
    private final Random random;
    private final Random qty;
    private final LineItem[] products;
    private final LineItemAvro[] productsAvro;

    public ProductGenerator() {
        URL resource = getClass().getClassLoader().getResource("data/products.json");
        ObjectMapper mapper = new ObjectMapper();
        random = new Random();
        qty = new Random();
        try {
            products = mapper.readValue(new File(resource.toURI()), LineItem[].class);
            productsAvro = mapper.readValue(new File(resource.toURI()), LineItemAvro[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getIndex() {
        return random.nextInt(100);
    }

    private int getQuantity() {
        return qty.nextInt(2) + 1;
    }

    public LineItem getNextProduct() {
        LineItem lineItem = products[getIndex()];
        lineItem.setItemQty(getQuantity());
        lineItem.setTotalValue(lineItem.getItemPrice() * lineItem.getItemQty());
        return lineItem;
    }

    public LineItemAvro getNextProductAvro() {
        LineItemAvro lineItem = productsAvro[getIndex()];
        lineItem.setItemQty(getQuantity());
        lineItem.setTotalValue(lineItem.getItemPrice() * lineItem.getItemQty());
        return lineItem;
    }
}
