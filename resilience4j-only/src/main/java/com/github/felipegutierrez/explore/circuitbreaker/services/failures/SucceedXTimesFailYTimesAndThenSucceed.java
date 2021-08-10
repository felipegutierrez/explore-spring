package com.github.felipegutierrez.explore.circuitbreaker.services.failures;

import com.github.felipegutierrez.explore.circuitbreaker.exceptions.FlightServiceException;

public class SucceedXTimesFailYTimesAndThenSucceed implements PotentialFailure {
    int successHowMany;
    int failHowMany;
    int successCount, failCount;

    public SucceedXTimesFailYTimesAndThenSucceed(int successHowMany, int failHowMany) {
        this.successHowMany = successHowMany;
        this.failHowMany = failHowMany;
    }

    @Override
    public void occur() {
        if (successCount < successHowMany) {
            successCount++;
            return;
        }
        if (failCount < failHowMany) {
            failCount++;
            throw new FlightServiceException("Flight search failed");
        }
        return;
    }
}
