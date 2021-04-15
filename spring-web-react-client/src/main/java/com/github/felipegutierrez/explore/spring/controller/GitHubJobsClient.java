package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.domain.GitHubPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
public class GitHubJobsClient {

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
}
