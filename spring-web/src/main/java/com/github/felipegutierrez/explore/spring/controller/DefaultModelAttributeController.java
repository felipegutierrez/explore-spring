package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.beans.Login;
import com.github.felipegutierrez.explore.spring.beans.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class DefaultModelAttributeController {

    @ModelAttribute("newuser")
    public User getDefaultUser() {
        return new User();
    }

    @ModelAttribute("genderItems")
    public List<String> getGenderItems() {
        return Arrays.asList("Male", "Female", "Other");
    }

    @ModelAttribute("login")
    public Login getDefaultLogin() {
        return new Login();
    }
}
