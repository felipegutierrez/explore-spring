package com.github.felipegutierrez.explore.spring.basics.services;

import com.github.felipegutierrez.explore.spring.ExploreSpringApplication;
import com.github.felipegutierrez.explore.spring.basics.beans.JdbcConnectionXml;
import com.github.felipegutierrez.explore.spring.basics.dao.PersonCdiDao;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SomeCdiBusinessTest {

    @Test
    void testHostnameFromProperties() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(ExploreSpringApplication.class)) {
            SomeCdiBusiness someCdiBusiness = applicationContext.getBean(SomeCdiBusiness.class);

            String result = someCdiBusiness.getHostname();
            String expected = "http://127.0.0.1:8080";

            assertEquals(expected, result);
        }
    }

    @Test
    void testPersonDaoWithMock() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(ExploreSpringApplication.class)) {

            PersonCdiDao personCdiDaoMock = mock(PersonCdiDao.class);
            when(personCdiDaoMock.getJdbcConnection()).thenReturn(new JdbcConnectionXml());

            SomeCdiBusiness someCdiBusiness = new SomeCdiBusiness(personCdiDaoMock);
            
            assertEquals(personCdiDaoMock.hashCode(), someCdiBusiness.getPersonCdiDao().hashCode());
        }
    }
}
