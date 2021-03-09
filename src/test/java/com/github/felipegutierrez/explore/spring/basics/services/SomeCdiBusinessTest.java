package com.github.felipegutierrez.explore.spring.basics.services;

import com.github.felipegutierrez.explore.spring.basics.beans.JdbcConnectionXml;
import com.github.felipegutierrez.explore.spring.basics.dao.PersonCdiDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SomeCdiBusinessTest {

    @Autowired
    SomeCdiBusiness someCdiBusiness;

    @Test
    void testHostnameFromProperties() {
        String result = someCdiBusiness.getHostname();
        String expected = "http://127.0.0.1:8080";

        assertEquals(expected, result);
    }

    @Test
    void testPersonDaoWithMock() {
        PersonCdiDao personCdiDaoMock = mock(PersonCdiDao.class);
        when(personCdiDaoMock.getJdbcConnection()).thenReturn(new JdbcConnectionXml());

        SomeCdiBusiness someCdiBusiness = new SomeCdiBusiness(personCdiDaoMock);

        assertEquals(personCdiDaoMock.hashCode(), someCdiBusiness.getPersonCdiDao().hashCode());
    }
}
