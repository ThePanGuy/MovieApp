package com.myprojects.demo.exceptions.config;

import com.myprojects.demo.exceptions.ExceptionResponse;
import com.myprojects.demo.exceptions.InvalidInputException;
import com.myprojects.demo.exceptions.UsernameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {UsernameException.class})
    protected ResponseEntity<Object> handleUsernameException(UsernameException e, WebRequest request) {
        log.error("Exception for request: {} message is {}", request, e.getMessage());
        return this.handleExceptionInternal(e,
                new ExceptionResponse(e.getMessage()),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(value = {InvalidInputException.class})
    protected ResponseEntity<Object> handleInvalidInputException(InvalidInputException e, WebRequest request) {
        log.error("Exception for request: {} message is {}", request, e.getMessage());
        return this.handleExceptionInternal(e,
                new ExceptionResponse(e.getMessage()),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleExceptions(Exception ex, WebRequest request) {
        log.error("Exception for request:{} message is:{}", request, ex.getMessage(), ex);
        return handleExceptionInternal(ex,
                new ExceptionResponse(
                        "There is an issue connecting to the services. Please refresh."),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }
}
