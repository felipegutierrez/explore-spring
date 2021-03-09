package com.github.felipegutierrez.explore.spring.basics.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SortAlgorithmImpl {

    @Qualifier("quickSortAlgorithm")
    @Autowired
    private final SortAlgorithm sortAlgorithm;

    public SortAlgorithmImpl(@Qualifier("quickSortAlgorithm") SortAlgorithm sortAlgorithm) {
        this.sortAlgorithm = sortAlgorithm;
    }
}
