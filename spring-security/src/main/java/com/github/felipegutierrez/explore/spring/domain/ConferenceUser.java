package com.github.felipegutierrez.explore.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
public class ConferenceUser {
    private String username;
    private String password;
    private List<String> submissions;
    private boolean speaker;
    private boolean admin;

    public ConferenceUser(ConferenceUser user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.submissions = user.getSubmissions();
        this.speaker = user.isSpeaker();
        this.admin = user.isAdmin();
    }
}
