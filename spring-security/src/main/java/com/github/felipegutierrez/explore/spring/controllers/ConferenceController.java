package com.github.felipegutierrez.explore.spring.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConferenceController {

    private String aboutConference = "Join us online September 1–2!";

    @GetMapping("/about")
    public String getAbout() {
        return this.aboutConference;
    }

    
}
