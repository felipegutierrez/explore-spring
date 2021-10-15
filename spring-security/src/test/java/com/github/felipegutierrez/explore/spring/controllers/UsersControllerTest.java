package com.github.felipegutierrez.explore.spring.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void statusUnauthorized() throws Exception {
        // http GET http://localhost:8083/users/status/check
        mockMvc.perform(get("/users/status/check"))
                .andExpect(status().isUnauthorized());
    }


}