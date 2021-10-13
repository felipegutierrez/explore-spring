package com.github.felipegutierrez.explore.spring.strategy;

import org.springframework.stereotype.Component;

@Component
public class DefaultWriter implements WriterPlugin {
    @Override
    public void write(String message) {
        System.out.println("writing DEFAULT: " + message);
    }

    @Override
    public boolean supports(String delimiter) {
        return delimiter.equalsIgnoreCase("default");
    }
}
