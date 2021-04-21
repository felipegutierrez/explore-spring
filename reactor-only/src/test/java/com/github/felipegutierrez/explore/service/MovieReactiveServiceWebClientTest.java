package com.github.felipegutierrez.explore.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieReactiveServiceWebClientTest {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();
    private final MovieInfoService movieInfoService = new MovieInfoService(webClient);
    private final ReviewService reviewService = new ReviewService(webClient);
    private final MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService, reviewService);

    @Test
    @Disabled("It is necessary to start the web client end-point: 'java -jar libs/reactive-movies-restful-api.jar'")
    void getAllMoviesWebClient() {
        var allMovies = movieReactiveService.getAllMoviesWebClient();
        StepVerifier.create(allMovies)
                .expectNextCount(7)
                .verifyComplete();
    }

    @Test
    @Disabled("It is necessary to start the web client end-point: 'java -jar libs/reactive-movies-restful-api.jar'")
    void getMovieByIdWebClient() {
        long movieId = 1l;
        var movieMono = movieReactiveService.getMovieByIdWebClient(movieId);

        StepVerifier.create(movieMono)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(1, movie.getReviewList().size());
                })
                .verifyComplete();
    }
}