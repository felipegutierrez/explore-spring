package com.github.felipegutierrez.explore.spring.basics.services;

import com.github.felipegutierrez.explore.spring.basics.dao.PersonCdiDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class SomeCdiBusinessTest {

    @Mock
    PersonCdiDao personCdiDaoMock;

    @InjectMocks
    SomeCdiBusiness someCdiBusiness;

    @Test
    void testPersonDaoWithMock() {
        SomeCdiBusiness someCdiBusiness = new SomeCdiBusiness(personCdiDaoMock);

        assertEquals(personCdiDaoMock.hashCode(), someCdiBusiness.getPersonCdiDao().hashCode());
    }

    @Test
    void testMockConnection() {
        Mockito.when(personCdiDaoMock.connectionExists()).thenReturn(true);
        assertTrue(personCdiDaoMock.connectionExists());
    }
}
