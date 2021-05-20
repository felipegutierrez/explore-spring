package com.github.felipegutierrez.explore.spring.adapter;

public class MoroOrange implements Orange {
    @Override
    public String getVariety() {
        return "Moro Blood Orange";
    }

    @Override
    public void eat() {
        System.out.println("Moro gets enjoyed");
    }

    @Override
    public void peel() {
        System.out.println("Moro gets peeled");
    }

    @Override
    public void juice() {
        System.out.println("Moro gets juiced");
    }
}
