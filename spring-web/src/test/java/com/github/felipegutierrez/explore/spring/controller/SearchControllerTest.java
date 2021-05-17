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
@AutoConfigureMockMvc
@SpringBootTest
class SearchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductRepository productRepository;

    @Test
    void searchCallable() throws Exception {
        mockMvc.perform(get("/search/callable?search=water"))
                .andExpect(status().isOk());
    }

    @Test
    void searchDeferred() throws Exception {
        mockMvc.perform(get("/search/deferred?search=water"))
                .andExpect(status().isOk());
    }

    @Test
    void search() throws Exception {
        mockMvc.perform(get("/search?search=water"))
                .andExpect(status().isOk());
    }
}