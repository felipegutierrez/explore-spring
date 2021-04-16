package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.domain.GitHubPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GitHubJobsClientTest {

    WebClient webClient = WebClient.create("https://jobs.github.com/");
    GitHubJobsClient gitHubJobsClient = new GitHubJobsClient(webClient);

    @Test
    void invokeGithubJobsApi() {
        int pageNum = 1;
        String description = "Java";

        List<GitHubPosition> gitHubPositionList = gitHubJobsClient
                .invokeGithubJobsApi(pageNum, description);
        gitHubPositionList.forEach(gitHubPosition -> {
            System.out.println("Title: " + gitHubPosition.getTitle());
            System.out.println("Location: " + gitHubPosition.getLocation());
        });

        assertTrue(gitHubPositionList.size() > 0);
        gitHubPositionList.forEach(Assertions::assertNotNull);
    }

    @Test
    @Disabled("Do not run in lower environment")
    void testInvokeGithubJobsApi() {
        List<Integer> pageNumbers = List.of(1, 2, 3);
        String description = "Java";

        List<GitHubPosition> gitHubPositionList = gitHubJobsClient
                .invokeGithubJobsApi(pageNumbers, description);
        gitHubPositionList.forEach(gitHubPosition -> {
            System.out.println("Title: " + gitHubPosition.getTitle());
            System.out.println("Location: " + gitHubPosition.getLocation());
        });

        assertTrue(gitHubPositionList.size() > 0);
        gitHubPositionList.forEach(Assertions::assertNotNull);
    }

    @Test
    void invokeGithubJobsApiAsync() {
        List<Integer> pageNumbers = List.of(1, 2, 3);
        String description = "Java";

        List<GitHubPosition> gitHubPositionList = gitHubJobsClient
                .invokeGithubJobsApiAsync(pageNumbers, description);
        gitHubPositionList.forEach(gitHubPosition -> {
            System.out.println("Title: " + gitHubPosition.getTitle());
            System.out.println("Location: " + gitHubPosition.getLocation());
        });

        assertTrue(gitHubPositionList.size() > 0);
        gitHubPositionList.forEach(Assertions::assertNotNull);
    }

    @Test
    @Disabled("Do not run in lower environment")
    void invokeGithubJobsApiAsyncAllCompletableFuture() {

        List<Integer> pageNumbers = List.of(1, 2, 3);
        String description = "Java";

        List<GitHubPosition> gitHubPositionList = gitHubJobsClient
                .invokeGithubJobsApiAsyncAllCompletableFuture(pageNumbers, description);
        gitHubPositionList.forEach(gitHubPosition -> {
            System.out.println("Title: " + gitHubPosition.getTitle());
            System.out.println("Location: " + gitHubPosition.getLocation());
        });

        assertTrue(gitHubPositionList.size() > 0);
        gitHubPositionList.forEach(Assertions::assertNotNull);
    }
}