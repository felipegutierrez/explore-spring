package com.github.felipegutierrez.explore.spring.basics.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcConnectionXml implements IJdbcConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConnectionXml.class);

    public JdbcConnectionXml() {
        LOGGER.info("This is my JdbcConnectionXml that is not a singleton bean.");
    }
}
