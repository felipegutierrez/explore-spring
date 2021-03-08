package com.github.felipegutierrez.explore.spring.basics.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SortAlgorithmImpl {

    @Qualifier("quickSortAlgorithm")
    @Autowired
    private final SortAlgorithm sortAlgorithm;

    public SortAlgorithmImpl(@Qualifier("quickSortAlgorithm") SortAlgorithm sortAlgorithm) {
        this.sortAlgorithm = sortAlgorithm;
    }
}
