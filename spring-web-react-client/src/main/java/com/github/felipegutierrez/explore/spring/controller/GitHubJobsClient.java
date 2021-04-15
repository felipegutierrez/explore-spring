package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.domain.GitHubPosition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
public class GitHubJobsClient {

    public static StopWatch stopWatch = new StopWatch();

    private final WebClient webClient;

    public GitHubJobsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<GitHubPosition> invokeGithubJobsApi(int pageNumber, String description) {
        String uri = UriComponentsBuilder.fromUriString("/positions.json")
                .queryParam("description", description)
                .queryParam("pageNum", pageNumber)
                .buildAndExpand()
                .toUriString();
        log.info("uri: {}", uri);
        List<GitHubPosition> gitHubPositionList = webClient.get().uri(uri)
                .retrieve()
                .bodyToFlux(GitHubPosition.class)
                .collectList()
                .block();
        return gitHubPositionList;
    }

    public List<GitHubPosition> invokeGithubJobsApi(List<Integer> pageNumbers, String description) {
        stopWatch.start();
        List<GitHubPosition> gitHubPositionList = pageNumbers
                .stream()
                .map(pageNumber -> invokeGithubJobsApi(pageNumber, description))
                .flatMap(Collection::stream) // same of: .flatMap(gitHubPositions -> gitHubPositions.stream())
                .collect(Collectors.toList());
        stopWatch.stop();
        log.info("time elapsed: " + stopWatch.getTime());
        return gitHubPositionList;
    }

    public List<GitHubPosition> invokeGithubJobsApiAsync(List<Integer> pageNumbers, String description) {
        stopWatch.start();
        List<CompletableFuture<List<GitHubPosition>>> gitHubPositionList = pageNumbers
                .stream()
                .map(pageNumber -> CompletableFuture.supplyAsync(() -> invokeGithubJobsApi(pageNumber, description)))
                .collect(Collectors.toList());

        List<GitHubPosition> gitHubPositions = gitHubPositionList
                .stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        stopWatch.stop();
        log.info("time elapsed: " + stopWatch.getTime());
        return gitHubPositions;
    }


    public List<GitHubPosition> invokeGithubJobsApiAsyncAllCompletableFuture(List<Integer> pageNumbers, String description) {
        stopWatch.start();
        List<CompletableFuture<List<GitHubPosition>>> gitHubPositionList = pageNumbers
                .stream()
                .map(pageNumber -> CompletableFuture.supplyAsync(() -> invokeGithubJobsApi(pageNumber, description)))
                .collect(Collectors.toList());

        List<GitHubPosition> gitHubPositions = CompletableFuture
                .allOf(gitHubPositionList.toArray(new CompletableFuture[gitHubPositionList.size()]))
                .thenApply(v -> gitHubPositionList
                        .stream()
                        .map(CompletableFuture::join)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList())
                )
                .join();

        stopWatch.stop();
        log.info("time elapsed: " + stopWatch.getTime());
        return gitHubPositions;
    }
}
