package com.github.felipegutierrez.explore.spring.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class DesignPatternsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void adoptPet_Dog() throws Exception {
        mockMvc.perform(post("/adoptPet/dog/raul"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.is("{\"name\":\"raul\",\"hungry\":false,\"type\":\"DOG\"}")))
                .andReturn();
    }

    @Test
    void adoptPet_Cat() throws Exception {
        mockMvc.perform(post("/adoptPet/cat/lucy"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.is("{\"name\":\"lucy\",\"hungry\":false,\"type\":\"CAT\"}")))
                .andReturn();
    }

    @Test
    void adoptPet_Exception() {

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(post("/adoptPet/mouse/jerry"));
        });

        String expectedMessage = "unknown animal type";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getPresidents() throws Exception {
        mockMvc.perform(get("/presidents"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.is("[{\"firstName\":\"George\",\"lastName\":\"Washington\",\"emailAddress\":null},{\"firstName\":\"John\",\"lastName\":\"Adams\",\"emailAddress\":null},{\"firstName\":\"Thomas\",\"lastName\":\"Jefferson\",\"emailAddress\":null}]")))
                .andReturn();
    }
}