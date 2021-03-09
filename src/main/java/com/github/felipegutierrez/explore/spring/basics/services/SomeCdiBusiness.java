package com.github.felipegutierrez.explore.spring.basics.services;

import com.github.felipegutierrez.explore.spring.basics.dao.PersonCdiDao;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SomeCdiBusiness {

    @Inject
    private PersonCdiDao personCdiDao;

    @Value("${app.hostname}")
    private String hostname;

    public SomeCdiBusiness(PersonCdiDao personCdiDao) {
        this.personCdiDao = personCdiDao;
    }

    public PersonCdiDao getPersonCdiDao() {
        return personCdiDao;
    }

    public void setPersonCdiDao(PersonCdiDao personCdiDao) {
        this.personCdiDao = personCdiDao;
    }

    public String getHostname() {
        return hostname;
    }
}
