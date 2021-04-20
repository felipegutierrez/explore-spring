package com.github.felipegutierrez.explore.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieReactiveServiceTest {

    private final MovieInfoService movieInfoService = new MovieInfoService();
    private final ReviewService reviewService = new ReviewService();
    private final MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService, reviewService);

    @Test
    void getAllMovies() {
        var allMovies = movieReactiveService.getAllMovies();
        StepVerifier.create(allMovies)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .assertNext(movie -> {
                    assertEquals("The Dark Knight", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .assertNext(movie -> {
                    assertEquals("Dark Knight Rises", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .verifyComplete();
    }
}