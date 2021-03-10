package com.github.felipegutierrez.explore.spring.basics.dao;

import com.github.felipegutierrez.explore.spring.basics.beans.IJdbcConnection;

import javax.inject.Named;

@Named
public class PersonCdiDao {

    private IJdbcConnection jdbcConnection;

    public IJdbcConnection getJdbcConnection() {
        return jdbcConnection;
    }

    public void setJdbcConnection(IJdbcConnection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
    }

    public boolean connectionExists() {
        return this.jdbcConnection.connectionExists();
    }
}
