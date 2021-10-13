package com.github.felipegutierrez.explore.spring.strategy;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;

@SpringBootApplication
@EnablePluginRegistries(WriterPlugin.class)
public class StrategyPatternWithSpringPlugin {

    public static void main(String[] args) {
        SpringApplication.run(StrategyPatternWithSpringPlugin.class, args);
    }

    @Bean
    ApplicationRunner runner(PluginRegistry<WriterPlugin, String> plugins) {
        return args -> {
            for (var format : "csv,txt,yaml".split(",")) {
                plugins.getPluginFor(format)
                        .orElseGet(() -> new DefaultWriter())
                        .write("Hello [" + format + "], Spring plugin with strategy pattern!");
            }
        };
    }
}
