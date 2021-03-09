package com.github.felipegutierrez.explore.spring.basics.dao;

import com.github.felipegutierrez.explore.spring.ExploreSpringApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class PersonDAOTest {

    @Test
    void personDAOMustHaveSingletonJdbcConnectionBeans() {
        try (ConfigurableApplicationContext applicationContext = SpringApplication.run(ExploreSpringApplication.class)) {

            PersonDao personDAO01 = applicationContext.getBean(PersonDao.class);
            PersonDao personDAO02 = applicationContext.getBean(PersonDao.class);

            assertEquals(personDAO01.hashCode(), personDAO02.hashCode());
            assertEquals(personDAO01.getJdbcConnectionProxyTarget().hashCode(), personDAO02.getJdbcConnectionProxyTarget().hashCode());
            assertNotEquals(personDAO01.getJdbcConnectionProxyInterface().hashCode(), personDAO02.getJdbcConnectionProxyInterface().hashCode());
        }
    }
}
