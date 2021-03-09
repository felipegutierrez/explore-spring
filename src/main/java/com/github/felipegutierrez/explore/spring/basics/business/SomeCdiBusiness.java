package com.github.felipegutierrez.explore.spring.basics.business;

import com.github.felipegutierrez.explore.spring.basics.dao.PersonCdiDao;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SomeCdiBusiness {

    @Inject
    private PersonCdiDao personCdiDao;

    public PersonCdiDao getPersonCdiDao() {
        return personCdiDao;
    }

    public void setPersonCdiDao(PersonCdiDao personCdiDao) {
        this.personCdiDao = personCdiDao;
    }
}
