package com.github.felipegutierrez.explore.spring.basics.dao;

import com.github.felipegutierrez.explore.spring.basics.beans.JdbcConnectionXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PersonXmlDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonXmlDao.class);

    private JdbcConnectionXml jdbcConnectionXml;

    public JdbcConnectionXml getJdbcConnectionXml() {
        return jdbcConnectionXml;
    }

    public void setJdbcConnectionXml(JdbcConnectionXml jdbcConnectionXml) {
        this.jdbcConnectionXml = jdbcConnectionXml;
    }
}
