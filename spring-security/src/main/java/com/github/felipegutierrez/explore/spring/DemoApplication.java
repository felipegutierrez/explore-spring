package com.github.felipegutierrez.explore.spring;

import com.github.felipegutierrez.explore.spring.pkce.PkceExample;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        PkceExample pkceExample = new PkceExample();
        pkceExample.run();
    }
}
