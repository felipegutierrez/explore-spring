package com.github.felipegutierrez.explore.spring.decorator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DecoratorTest {

    @Test
    public void testDecorator() {
        Pizza pizza = new ThickCrustPizza();
        assertEquals("Thick Crust Pizza", pizza.getDescription());
        assertEquals(BigDecimal.valueOf(15), pizza.getCost());

        Pepperoni pepperoni = new Pepperoni(pizza);
        assertEquals("Thick Crust Pizza + pepperoni", pepperoni.getDescription());
        assertEquals(BigDecimal.valueOf(16.5), pepperoni.getCost());

        Pepperoni doublePepperoni = new Pepperoni(pepperoni);
        assertEquals("Thick Crust Pizza + pepperoni + pepperoni", doublePepperoni.getDescription());
        assertEquals(BigDecimal.valueOf(18.0), doublePepperoni.getCost());
    }
}
