package com.github.felipegutierrez.explore.spring.factory;

public class Cat implements Pet {
    private String name;
    private boolean hungry;

    public Cat(){
        super();
        this.hungry = true;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getType() {
        return "CAT";
    }

    @Override
    public boolean isHungry() {
        return this.hungry;
    }

    @Override
    public void feed() {
        this.hungry = false;
    }
}
