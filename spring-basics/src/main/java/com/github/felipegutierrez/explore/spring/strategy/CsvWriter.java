package com.github.felipegutierrez.explore.spring.strategy;

import org.springframework.stereotype.Component;

@Component
public class CsvWriter implements WriterPlugin {
    @Override
    public void write(String message) {
        System.out.println("writing CSV: " + message);
    }

    @Override
    public boolean supports(String delimiter) {
        return delimiter.equalsIgnoreCase("csv");
    }
}
