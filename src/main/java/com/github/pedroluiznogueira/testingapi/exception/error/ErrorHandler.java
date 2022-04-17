package com.github.pedroluiznogueira.testingapi.exception.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> global(Exception exception, WebRequest request) {
        Error error = Error.builder()
                .timestamp(new Date())
                .message(exception.getMessage())
                .detail(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
