package com.github.felipegutierrez.explore.service;

import com.github.felipegutierrez.explore.exception.MovieException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;

@ExtendWith(MockitoExtension.class)
class MovieReactiveServiceMockTest {

    @Mock
    MovieInfoService movieInfoService;

    @Mock
    ReviewService reviewService;

    @InjectMocks
    MovieReactiveService movieReactiveService;

    @Test
    void getAllMovies() {

        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenCallRealMethod();

        var movieFlux = movieReactiveService.getAllMovies();

        StepVerifier.create(movieFlux)
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void getAllMoviesException() {

        var errorMsg = "this is an error message for the movie service";
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new RuntimeException(errorMsg));

        var movieFlux = movieReactiveService.getAllMovies();

        StepVerifier.create(movieFlux)
                .expectSubscription()
                .expectError(MovieException.class)
                .verify();

        Mockito.verify(reviewService, Mockito.times(4))
                .retrieveReviewsFlux(isA(Long.class));

        StepVerifier.create(movieFlux)
                .expectSubscription()
                .expectErrorMessage(errorMsg)
                .verify();
    }
}