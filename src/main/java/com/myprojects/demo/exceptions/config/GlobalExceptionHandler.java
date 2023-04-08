package com.myprojects.demo.exceptions.config;

import com.myprojects.demo.exceptions.ExceptionResponse;
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
}
