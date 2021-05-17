package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.beans.Login;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class UserProfileController {

    @PostMapping("/userprofile")
    public String getUserProfile(@SessionAttribute("login") Login login, Model model) {
        System.out.println("in user profile controller");
        System.out.println("username from session: " + login.getUsername());
        //call to userrepository to get all the user information
        model.addAttribute("username", login.getUsername());
        return "profile";
    }
}
