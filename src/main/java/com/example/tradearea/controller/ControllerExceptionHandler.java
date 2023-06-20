package com.example.tradearea.controller;



import com.example.tradearea.service.exceptions.DBException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.bind.ValidationException;


@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DBException.class)
    public ResponseEntity<String> handleDBFoundException(DBException ex) {

        return switch (ex.getOperationType()) {
            case CREATE, UPDATE, DELETE -> new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            case READE_ONE, READE_PAGE -> new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        };
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String  otherExceptions(RuntimeException ex) {
        return ex.getMessage();
    }

}
