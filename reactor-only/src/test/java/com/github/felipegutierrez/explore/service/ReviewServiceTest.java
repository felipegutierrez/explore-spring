package com.github.felipegutierrez.explore.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

class ReviewServiceTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    ReviewService reviewService = new ReviewService(webClient);

    @Test
    @Disabled("It is necessary to start the web client end-point: 'java -jar libs/reactive-movies-restful-api.jar'")
    void retrieveReviewsFluxWebClient() {
        var reviewFlux = reviewService.retrieveReviewsFluxWebClient(1l);

        StepVerifier.create(reviewFlux)
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(reviewFlux)
                .expectNextMatches(review -> review.getMovieInfoId().equals(1l))
                .verifyComplete();
    }
}