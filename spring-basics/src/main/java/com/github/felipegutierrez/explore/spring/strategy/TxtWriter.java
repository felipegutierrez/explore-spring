package com.github.felipegutierrez.explore.spring.strategy;

import org.springframework.stereotype.Component;

@Component
public class TxtWriter implements WriterPlugin {
    @Override
    public void write(String message) {
        System.out.println("writing TXT: " + message);
    }

    @Override
    public boolean supports(String delimiter) {
        return delimiter.equalsIgnoreCase("txt");
    }
}
