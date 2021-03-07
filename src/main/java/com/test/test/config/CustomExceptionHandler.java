package com.test.test.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked","rawtypes"})
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);
    Map<String,String> error = new HashMap<>();
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

        log.error(ex.getMessage());

        error.put("error",ex.getLocalizedMessage());
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotSavedException.class)
    public final ResponseEntity<Object> userNotSavedException(UserNotSavedException ex, WebRequest request) {

        log.error(ex.getMessage());
        error.put("error",ex.getLocalizedMessage());
        return new ResponseEntity(error, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Object> userDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {

        log.error(ex.getMessage());
        error.put("error",ex.getLocalizedMessage());
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
