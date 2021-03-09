package com.github.felipegutierrez.explore.spring.basics.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JdbcConnectionProxyTarget {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConnectionProxyTarget.class);

    public JdbcConnectionProxyTarget() {
        // LOGGER.info("This is my JdbcConnection that is not a singleton bean.");
    }

    @PostConstruct
    public void firstMethod() {
        LOGGER.info("This is the first method to be executed at JdbcConnectionProxyTarget");
    }

    @PreDestroy
    public void finalMethod() {
        LOGGER.info("This is the final method to be executed at JdbcConnectionProxyTarget");
    }

}
