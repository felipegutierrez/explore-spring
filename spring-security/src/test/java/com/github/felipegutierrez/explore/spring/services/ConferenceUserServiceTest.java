package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.domain.ConferenceUser;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ConferenceUserServiceTest {

    @Test
    public void userGetsRoleAttendeeByDefault() {
        ConferenceUser user = new ConferenceUser();
        ConferenceUserService.ConferenceUserDetails userDetails = new ConferenceUserService.ConferenceUserDetails(user);
        Set<String> authorities = AuthorityUtils.authorityListToSet(userDetails.getAuthorities());
        assertThat(authorities).contains("ROLE_ATTENDEE");
    }

    @Test
    public void adminGetsRoleAdmin() {
        ConferenceUser user = new ConferenceUser();
        user.setAdmin(true);
        ConferenceUserService.ConferenceUserDetails userDetails = new ConferenceUserService.ConferenceUserDetails(user);
        Set<String> authorities = AuthorityUtils.authorityListToSet(userDetails.getAuthorities());
        assertThat(authorities).contains("ROLE_ADMIN");
    }

    @Test
    public void speakerGetsRoleSpeaker() {
        ConferenceUser user = new ConferenceUser();
        user.setSpeaker(true);
        ConferenceUserService.ConferenceUserDetails userDetails = new ConferenceUserService.ConferenceUserDetails(user);
        Set<String> authorities = AuthorityUtils.authorityListToSet(userDetails.getAuthorities());
        assertThat(authorities).contains("ROLE_SPEAKER");
    }

    @Test
    public void userDetailsHasTheSameValuesAsConferenceUser() {
        ConferenceUser user = new ConferenceUser();
        user.setUsername("user");
        user.setPassword("password");
        user.setSubmissions(List.of("Talk 1"));
        ConferenceUserService.ConferenceUserDetails userDetails = new ConferenceUserService.ConferenceUserDetails(user);
        assertThat(userDetails.getUsername()).isEqualTo("user");
        assertThat(userDetails.getPassword()).isEqualTo("password");
        assertThat(userDetails.getSubmissions()).isEqualTo(List.of("Talk 1"));
    }
}