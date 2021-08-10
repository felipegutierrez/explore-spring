package com.github.felipegutierrez.explore.circuitbreaker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CircuitBreakerDemoTest {

    @Test
    void circuitBreakerOpenAndThenClose_onlySuccess() {
        CircuitBreakerDemo circuitBreakerDemo = new CircuitBreakerDemo();
        circuitBreakerDemo.circuitBreakerOpenAndThenClose(4);
        assertEquals(4, circuitBreakerDemo.getCircuitBreaker().getMetrics().getNumberOfSuccessfulCalls());
        assertEquals(0, circuitBreakerDemo.getCircuitBreaker().getMetrics().getNumberOfFailedCalls());
        assertEquals(4, circuitBreakerDemo.getCircuitBreaker().getMetrics().getNumberOfBufferedCalls());
    }

    @Test
    void circuitBreakerOpenAndThenClose_successAndFailureCalls() {
        CircuitBreakerDemo circuitBreakerDemo = new CircuitBreakerDemo();

        circuitBreakerDemo.circuitBreakerOpenAndThenClose(6);
        assertEquals(4, circuitBreakerDemo.getCircuitBreaker().getMetrics().getNumberOfSuccessfulCalls());
        assertEquals(2, circuitBreakerDemo.getCircuitBreaker().getMetrics().getNumberOfFailedCalls());
        assertEquals(6, circuitBreakerDemo.getCircuitBreaker().getMetrics().getNumberOfBufferedCalls());
    }
}