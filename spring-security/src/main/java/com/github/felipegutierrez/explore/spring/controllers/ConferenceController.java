package com.github.felipegutierrez.explore.spring.controllers;

import com.github.felipegutierrez.explore.spring.domain.ConferenceUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConferenceController {

    private String aboutConference = "Join us online September 1–2!";

    @GetMapping("/about")
    public String getAbout() {
        return this.aboutConference;
    }

    @PostMapping("/about")
    public void updateAbout(@RequestBody String updatedAbout) {
        this.aboutConference = updatedAbout;
    }

    @GetMapping("/greeting")
    public String greeting(@AuthenticationPrincipal(expression = "username") String username) {
        return "Hello, " + username + "!";
    }

    @GetMapping("/submissions")
    public List<String> submissions(@AuthenticationPrincipal ConferenceUser user) {
        return user.getSubmissions();
    }
}
