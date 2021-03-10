package com.github.felipegutierrez.explore.spring.basics.beans;

public interface IJdbcConnection {

    boolean connectionExists();

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();
}
