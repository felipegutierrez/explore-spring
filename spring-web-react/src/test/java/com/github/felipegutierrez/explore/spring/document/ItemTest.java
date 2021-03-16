package com.github.felipegutierrez.explore.spring.document;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemTest {

    @Test
    public void testItemGetters() {
        Item item = new Item("id", "desc", 45.6);
        assertEquals(item.getId(), "id");
        assertEquals(item.getDescription(), "desc");
        assertEquals(item.getPrice(), Double.valueOf(45.6));
    }

    @Test
    public void testItemSetters() {
        Item item = new Item();
        item.setId("id");
        item.setDescription("desc");
        item.setPrice(45.6);
        assertEquals(item.getId(), "id");
        assertEquals(item.getDescription(), "desc");
        assertEquals(item.getPrice(), Double.valueOf(45.6));
    }
}
