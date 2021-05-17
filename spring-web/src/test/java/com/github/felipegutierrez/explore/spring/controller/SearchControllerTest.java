package com.github.felipegutierrez.explore.spring.controller;

import com.github.felipegutierrez.explore.spring.beans.Product;
import com.github.felipegutierrez.explore.spring.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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

        Product product = new Product().setId(1).setImagePath("path").setName("name");

        Mockito.when(productRepository.searchByName(any()))
                .thenReturn(List.of(product));

        MvcResult result = mockMvc.perform(get("/search/callable?search=water"))
                .andExpect(status().isOk())
                .andReturn()
                ;
    }

    @Test
    void searchDeferred() throws Exception {
        Product product = new Product().setId(1).setImagePath("path").setName("name");

        Mockito.when(productRepository.searchByName(any()))
                .thenReturn(List.of(product));

        MvcResult result = mockMvc.perform(get("/search/deferred?search=water"))
                .andExpect(status().isOk())
                .andReturn()
                ;
    }

    @Test
    void search() throws Exception {

        Product product = new Product().setId(1).setImagePath("path").setName("name");

        Mockito.when(productRepository.searchByName(any()))
                .thenReturn(List.of(product));

        MvcResult result = mockMvc.perform(get("/search?search=water"))
                .andExpect(status().isOk())
                .andReturn()
//                .andExpect(new ResultMatcher() {
//                    @Override
//                    public void match(MvcResult result) throws Exception {
//                        System.out.println("content type: " + result.getResponse().getContentAsString());
//                    }
//                })
                ;
    }
}