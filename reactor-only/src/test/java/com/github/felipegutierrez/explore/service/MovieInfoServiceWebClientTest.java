package com.github.felipegutierrez.explore.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

class MovieInfoServiceWebClientTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    MovieInfoService movieInfoService = new MovieInfoService(webClient);

    @Test
    @Disabled("It is necessary to start the web client end-point: 'java -jar libs/reactive-movies-restful-api.jar'")
    void retrieveMoviesFluxWebClient() {
        var movieInfoFlux = movieInfoService.retrieveMoviesFluxWebClient();

        StepVerifier.create(movieInfoFlux)
                .expectNextCount(7)
                .verifyComplete();
    }

    @Test
    @Disabled("It is necessary to start the web client end-point: 'java -jar libs/reactive-movies-restful-api.jar'")
    void retrieveMovieInfoMonoUsingIdWebClient() {
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingIdWebClient(1);

        StepVerifier.create(movieInfoMono)
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(movieInfoMono)
                .expectNextMatches(movieInfo -> movieInfo.getName().equals("Batman Begins"))
                .verifyComplete();
    }
}