package com.github.felipegutierrez.explore.spring.decorator;

import java.math.BigDecimal;

public class Pepperoni extends PizzaIngredientDecorator {
    private final Pizza pizza;

    public Pepperoni(Pizza pizza) {
        super();
        this.pizza = pizza;
    }

    @Override
    public String getDescription() {
        return this.pizza.getDescription() + " + pepperoni";
    }

    @Override
    public BigDecimal getCost() {
        return (new BigDecimal(1.50)).add(this.pizza.getCost());
    }
}
