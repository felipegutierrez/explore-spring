package com.github.felipegutierrez.explore.spring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler({ApplicationException.class, AsyncRequestTimeoutException.class})
    public String handleException() {
        System.out.println("in global exception handler");
        return "error";
    }

    @ExceptionHandler(LoginFailureException.class)
    public ResponseEntity handleLoginFailure(LoginFailureException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
