package com.github.felipegutierrez.explore.spring.strategy;

import org.springframework.plugin.core.Plugin;

public interface WriterPlugin extends Plugin<String> {
    void write(String message);
}
