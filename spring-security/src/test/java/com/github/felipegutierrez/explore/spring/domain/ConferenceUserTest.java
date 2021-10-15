package com.github.felipegutierrez.explore.spring.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConferenceUserTests {

    @Test
    public void constructorCreatesCopy() {
        ConferenceUser user = ConferenceUser.builder()
                .username("user")
                .password("password")
                .submissions(List.of("Talk 1"))
                .speaker(true)
                .admin(true)
                .build();
        ConferenceUser copy = user.toBuilder().build();
        assertThat(user).usingRecursiveComparison().isEqualTo(copy);
    }
}