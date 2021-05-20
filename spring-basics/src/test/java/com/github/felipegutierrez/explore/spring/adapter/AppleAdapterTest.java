package com.github.felipegutierrez.explore.spring.adapter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AppleAdapterTest {

    @Test
    public void testAdapter() {
        Orange orange = new MoroOrange();
        Apple apple = new AppleAdapter(orange);

        assertEquals("Moro Blood Orange", apple.getVariety());
        apple.eat();
    }
}
