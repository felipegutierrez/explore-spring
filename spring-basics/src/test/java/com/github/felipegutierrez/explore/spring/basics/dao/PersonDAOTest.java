package com.github.felipegutierrez.explore.spring.basics.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class PersonDAOTest {

    @Autowired
    PersonDao personDAO01;

    @Autowired
    PersonDao personDAO02;

    @Test
    void personDAOMustHaveSingletonJdbcConnectionBeans() {
        assertEquals(personDAO01.hashCode(), personDAO02.hashCode());
        assertEquals(personDAO01.getJdbcConnectionProxyTarget().hashCode(), personDAO02.getJdbcConnectionProxyTarget().hashCode());
        assertNotEquals(personDAO01.getJdbcConnectionProxyInterface().hashCode(), personDAO02.getJdbcConnectionProxyInterface().hashCode());
    }
}
