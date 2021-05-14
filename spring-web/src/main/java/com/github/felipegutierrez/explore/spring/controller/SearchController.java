package com.github.felipegutierrez.explore.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {

    @GetMapping("/search")
    public String search(@RequestParam("search") String search, Model model){
        System.out.println("in search controller");
        System.out.println("search criteria: "+search);
        return "search";
    }

}
