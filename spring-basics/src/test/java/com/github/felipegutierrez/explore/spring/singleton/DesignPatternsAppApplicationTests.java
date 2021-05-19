package com.github.felipegutierrez.explore.spring.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DesignPatternsAppApplicationTests {

    @Autowired
    SingB singB1;
    @Autowired
    SingB singB2;

    @Test
    public void testSpringSingletons() {
        assertNotNull(singB1);
        assertNotNull(singB2);
        assertEquals(singB1, singB2);
    }

    @Test
    public void testSingletons() {
        SingA singA1 = SingA.getInstance();
        SingA singA2 = SingA.getInstance();

        assertNotNull(singA1);
        assertNotNull(singA2);
        assertEquals(singA1, singA2);
    }
}

