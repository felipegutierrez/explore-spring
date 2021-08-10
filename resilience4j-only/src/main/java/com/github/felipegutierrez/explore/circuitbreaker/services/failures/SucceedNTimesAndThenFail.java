package com.github.felipegutierrez.explore.circuitbreaker.services.failures;

import com.github.felipegutierrez.explore.circuitbreaker.exceptions.FlightServiceException;

public class SucceedNTimesAndThenFail implements PotentialFailure {

    int n;
    int successCount;

    public SucceedNTimesAndThenFail(int n) {
        this.n = n;
    }

    @Override
    public void occur() {
        if (successCount < n) {
            successCount++;
            return;
        }
        throw new FlightServiceException("Error occurred during flight search");
    }
}
