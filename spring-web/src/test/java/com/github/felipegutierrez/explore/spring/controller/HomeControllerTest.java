package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductRepository productRepository;

    @Test
    void goHome() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());
    }

    @Test
    void goToSearch() throws Exception {
        mockMvc.perform(get("/goToSearch"))
                .andExpect(status().isOk());
    }

    @Test
    void goToLogin() throws Exception {
        mockMvc.perform(get("/goToLogin"))
                .andExpect(status().isOk());
    }

    @Test
    void goToRegistration() throws Exception {
        mockMvc.perform(get("/goToRegistration"))
                .andExpect(status().isOk());
    }
}