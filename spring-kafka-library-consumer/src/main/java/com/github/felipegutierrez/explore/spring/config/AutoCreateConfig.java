package com.github.felipegutierrez.explore.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
@Profile("dev")
public class AutoCreateConfig {

}
