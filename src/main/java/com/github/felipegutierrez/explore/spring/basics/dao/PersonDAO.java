package com.github.felipegutierrez.explore.spring.basics.dao;

import com.github.felipegutierrez.explore.spring.basics.beans.IJdbcConnection;
import com.github.felipegutierrez.explore.spring.basics.beans.JdbcConnectionProxyTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PersonDAO {

    @Autowired
    @Qualifier("jdbcConnectionProxyTarget")
    private JdbcConnectionProxyTarget jdbcConnectionProxyTarget;

    @Autowired
    @Qualifier("jdbcConnectionProxyInterface")
    private IJdbcConnection jdbcConnectionProxyInterface;


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
