package com.github.felipegutierrez.explore.spring.basics.dao;

import com.github.felipegutierrez.explore.spring.ExploreSpringApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
// @ContextConfiguration("/applicationContext.xml")
public class PersonXmlDAOTest {

    /**
     * unfortunately gradle with jdk 8 does not find applicationContext.xml.
     * It only works with jdk 11.
     */
    /*
    @Autowired
    PersonXmlDao personXmlDAO01;

    @Autowired
    PersonXmlDao personXmlDAO02;

    @Test
    void personDAOMustHaveSingletonJdbcConnectionBeans() {
        assertEquals(personXmlDAO01.hashCode(), personXmlDAO02.hashCode());
        assertEquals(personXmlDAO01.getJdbcConnectionXml().hashCode(), personXmlDAO02.getJdbcConnectionXml().hashCode());
    }
    */
}
