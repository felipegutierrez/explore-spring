package com.github.felipegutierrez.explore.spring.cloud.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class AlbumService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumService.class);

    private RestTemplate restTemplate = new RestTemplate();

    private CircuitBreakerFactory circuitBreakerFactory;

    private CircuitBreaker circuitBreaker;

    @Autowired
    public AlbumService(CircuitBreakerFactory circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
    }

    public String getAlbumList() {
        return getAlbumList("https://jsonplaceholder.typicode.com/albums");
    }

    public String getAlbumList(String url) {
        // CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");

        return circuitBreaker.run(() -> restTemplate.getForObject(url, String.class),
                throwable -> getDefaultAlbumList());
    }

    private String getDefaultAlbumList() {
        try {
            System.out.println("getDefaultAlbumList");
            return new String(Files.readAllBytes(
                    Paths.get(getClass().getClassLoader().getResource("data/fallback-album-list.json").toURI())
            ));
        } catch (Exception e) {
            LOGGER.error("error occurred while reading the file", e);
        }
        return null;
    }
}
