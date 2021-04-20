package com.github.felipegutierrez.explore.service;

import com.github.felipegutierrez.explore.domain.Movie;
import com.github.felipegutierrez.explore.exception.MovieException;
import com.github.felipegutierrez.explore.exception.NetworkException;
import com.github.felipegutierrez.explore.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;

@Slf4j
public class MovieReactiveService {

    private final MovieInfoService movieInfoService;
    private final ReviewService reviewService;

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
    }

    public Flux<Movie> getAllMovies() {
        var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    var reviewListMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewListMono
                            .map(reviewsList -> new Movie(movieInfo, reviewsList));
                })
                .onErrorMap(ex -> {
                    log.error("Exception occurred: {}", ex);
                    throw new MovieException(ex.getMessage());
                })
                .retry(3)
                .log();
    }

    public Flux<Movie> getAllMoviesWithRetry() {
        var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    var reviewListMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewListMono
                            .map(reviewsList -> new Movie(movieInfo, reviewsList));
                })
                .onErrorMap(ex -> {
                    log.error("Exception occurred: {}", ex);
                    if (ex instanceof NetworkException) throw new MovieException(ex.getMessage());
                    else throw new ServiceException(ex.getMessage());
                })
                .retryWhen(getRetryBackoffSpec())
                .log();
    }

    private RetryBackoffSpec getRetryBackoffSpec() {
        var retryConfig = Retry.fixedDelay(3, Duration.ofMillis(400))
                .filter(ex -> ex instanceof MovieException)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                        Exceptions.propagate(retrySignal.failure()));
        return retryConfig;
    }

    public Mono<Movie> getMovieById(long movieId) {
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var movieReviewsFlux = reviewService.retrieveReviewsFlux(movieId).collectList();
        return movieInfoMono.zipWith(movieReviewsFlux, (movieInfo, movieReviews) -> new Movie(movieInfo, movieReviews))
                .log();
    }

    public Mono<Movie> getMovieByIdUsingFlatmap(long movieId) {
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        return movieInfoMono.flatMap(movieInfo -> {
            var movieReviews = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                    .collectList();
            return movieReviews.map(reviewList -> new Movie(movieInfo, reviewList));
        });
    }
}
