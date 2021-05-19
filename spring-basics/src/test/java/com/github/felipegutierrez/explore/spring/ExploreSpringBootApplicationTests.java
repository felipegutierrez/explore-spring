package com.github.felipegutierrez.explore.spring;

import com.github.felipegutierrez.explore.spring.prototype.ProtoFalse;
import com.github.felipegutierrez.explore.spring.prototype.ProtoTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class ExploreSpringBootApplicationTests {

    @Autowired
    ProtoTrue true1;
    @Autowired
    ProtoTrue true2;

    @Autowired
    ProtoFalse false1;
    @Autowired
    ProtoFalse false2;

    @Test
    public void testPrototypes() {
        assertEquals(false1, false2);
        assertNotEquals(true1, true2);
    }
}
