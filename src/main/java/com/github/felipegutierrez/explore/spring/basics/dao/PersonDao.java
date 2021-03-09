package com.github.felipegutierrez.explore.spring.basics.dao;

import com.github.felipegutierrez.explore.spring.basics.beans.IJdbcConnection;
import com.github.felipegutierrez.explore.spring.basics.beans.JdbcConnectionProxyTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class PersonDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonDao.class);

    @Autowired
    @Qualifier("jdbcConnectionProxyTarget")
    private JdbcConnectionProxyTarget jdbcConnectionProxyTarget;

    @Autowired
    @Qualifier("jdbcConnectionProxyInterface")
    private IJdbcConnection jdbcConnectionProxyInterface;

    @PostConstruct
    public void firstMethod() {
        LOGGER.info("This is the first method to be executed at PersonDAO");
    }

    @PreDestroy
    public void finalMethod() {
        LOGGER.info("This is the final method to be executed at PersonDAO");
    }

    public JdbcConnectionProxyTarget getJdbcConnectionProxyTarget() {
        return jdbcConnectionProxyTarget;
    }

    public void setJdbcConnectionProxyTarget(JdbcConnectionProxyTarget jdbcConnectionProxyTarget) {
        this.jdbcConnectionProxyTarget = jdbcConnectionProxyTarget;
    }

    public IJdbcConnection getJdbcConnectionProxyInterface() {
        return jdbcConnectionProxyInterface;
    }

    public void setJdbcConnectionProxyInterface(IJdbcConnection jdbcConnectionProxyInterface) {
        this.jdbcConnectionProxyInterface = jdbcConnectionProxyInterface;
    }
}
