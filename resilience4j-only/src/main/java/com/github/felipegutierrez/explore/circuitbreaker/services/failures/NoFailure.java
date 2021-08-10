package com.github.felipegutierrez.explore.circuitbreaker.services.failures;

public class NoFailure implements PotentialFailure {
    @Override
    public void occur() {
    }
}
