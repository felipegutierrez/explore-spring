package com.github.felipegutierrez.explore.spring.basics.dao;

import com.github.felipegutierrez.explore.spring.ExploreSpringApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class PersonDAOTest {

    ApplicationContext applicationContext = SpringApplication.run(ExploreSpringApplication.class);

    @Test
    void personDAOMustHaveSingletonJdbcConnectionBeans() {
        PersonDAO personDAO01 = applicationContext.getBean(PersonDAO.class);
        PersonDAO personDAO02 = applicationContext.getBean(PersonDAO.class);

        assertEquals(personDAO01.hashCode(), personDAO02.hashCode());
        assertEquals(personDAO01.getJdbcConnectionProxyTarget().hashCode(), personDAO02.getJdbcConnectionProxyTarget().hashCode());
        assertNotEquals(personDAO01.getJdbcConnectionProxyInterface().hashCode(), personDAO02.getJdbcConnectionProxyInterface().hashCode());
    }
}
