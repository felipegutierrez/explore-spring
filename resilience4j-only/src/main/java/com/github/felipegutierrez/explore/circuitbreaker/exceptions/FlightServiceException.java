package com.github.felipegutierrez.explore.circuitbreaker.exceptions;

public class FlightServiceException extends RuntimeException {
    public FlightServiceException(String message) {
        super(message);
    }
}
