package com.github.felipegutierrez.explore.spring.basics.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class PersonXmlDAOTest {

    @Autowired
    PersonXmlDao personXmlDAO01;

    @Autowired
    PersonXmlDao personXmlDAO02;

    @Test
    void personDAOMustHaveSingletonJdbcConnectionBeans() {
        assertEquals(personXmlDAO01.hashCode(), personXmlDAO02.hashCode());
        assertEquals(personXmlDAO01.getJdbcConnectionXml().hashCode(), personXmlDAO02.getJdbcConnectionXml().hashCode());
        // assertNotEquals(personXmlDAO01.getJdbcConnectionXml().hashCode(), personXmlDAO02.getJdbcConnectionXml().hashCode());
    }
}
