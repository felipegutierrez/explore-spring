package com.github.felipegutierrez.explore.spring.cloud.controllers;

import com.github.felipegutierrez.explore.spring.cloud.services.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlbumController {

    @Autowired
    private AlbumService service;

    @GetMapping("/albums")
    public String albums() {
        return service.getAlbumList();
    }
}
