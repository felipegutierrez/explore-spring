package com.github.felipegutierrez.explore.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectionController {

    @GetMapping("/redirectToLinkedIn")
    public String redirectOut() {
        System.out.println("in redirect controller");
        return "redirect:https://www.linkedin.com/in/felipe-gutierrez-624b1a14";
    }
}
