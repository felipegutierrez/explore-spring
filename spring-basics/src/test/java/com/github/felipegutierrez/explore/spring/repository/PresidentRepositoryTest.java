package com.github.felipegutierrez.explore.spring.repository;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class PresidentRepositoryTest {

    @Autowired
    PresidentRepository presidentRepository;

    @Test
    public void testRepository() {

        String presidentWashington = presidentRepository.findById(1L).get().getFirstName() + " " + presidentRepository.findById(1L).get().getLastName();
        assertEquals("George Washington", presidentWashington);

        String presidentAdams = presidentRepository.findById(2L).get().getFirstName() + " " + presidentRepository.findById(2L).get().getLastName();
        assertEquals("John Adams", presidentAdams);

        Iterator<PresidentEntity> presidentEntityIterator = presidentRepository.findAll().iterator();
        assertEquals(45, Lists.newArrayList(presidentEntityIterator).size());
    }
}