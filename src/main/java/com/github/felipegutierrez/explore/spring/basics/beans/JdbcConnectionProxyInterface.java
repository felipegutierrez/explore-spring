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
        proxyMode = ScopedProxyMode.INTERFACES)
public class JdbcConnectionProxyInterface implements IJdbcConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConnectionProxyInterface.class);

    public JdbcConnectionProxyInterface() {
        // LOGGER.info("This is my JdbcConnection that is not a singleton bean.");
    }

    @PostConstruct
    public void firstMethod() {
        LOGGER.info("This is the first method to be executed at JdbcConnectionProxyInterface");
    }

    @PreDestroy
    public void finalMethod() {
        LOGGER.info("This is the final method to be executed at JdbcConnectionProxyInterface");
    }
}
