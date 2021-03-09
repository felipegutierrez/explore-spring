package com.github.felipegutierrez.explore.spring.basics.dao;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = PersonXmlDAOTest.class)
public class PersonXmlDAOTest {

    @Test
    void personDAOMustHaveSingletonJdbcConnectionBeans() {
        try (ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml")) {

            PersonXmlDao personXmlDAO01 = applicationContext.getBean(PersonXmlDao.class);
            PersonXmlDao personXmlDAO02 = applicationContext.getBean(PersonXmlDao.class);

            assertEquals(personXmlDAO01.hashCode(), personXmlDAO02.hashCode());
            assertEquals(personXmlDAO01.getJdbcConnectionXml().hashCode(), personXmlDAO02.getJdbcConnectionXml().hashCode());
            // assertNotEquals(personXmlDAO01.getJdbcConnectionXml().hashCode(), personXmlDAO02.getJdbcConnectionXml().hashCode());
        }
    }
}
