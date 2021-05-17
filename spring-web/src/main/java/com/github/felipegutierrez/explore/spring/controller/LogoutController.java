package com.github.felipegutierrez.explore.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        System.out.println("Ending user session");
        session.invalidate();
        // does not work because the session ended
        // System.out.println(session.getAttribute("login"));
        return "login";
    }
}
