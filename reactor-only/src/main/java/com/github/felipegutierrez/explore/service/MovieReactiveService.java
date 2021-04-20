package com.github.felipegutierrez.explore.service;

import com.github.felipegutierrez.explore.domain.Movie;
import com.github.felipegutierrez.explore.exception.MovieException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                .log();
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
