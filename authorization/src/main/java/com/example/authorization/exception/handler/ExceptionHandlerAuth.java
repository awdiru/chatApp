package com.example.authorization.exception.handler;

import com.example.template.model.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAuth {
    private final String path = "/auth";

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return response(HttpStatus.BAD_REQUEST, e.getMessage(), path);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException e) {
        return response(HttpStatus.CONFLICT, e.getMessage(), path);
    }

    private static ResponseEntity<ExceptionResponse> response(HttpStatus status, String message, String path) {
        ExceptionResponse response = new ExceptionResponse(status, message, path);
        return new ResponseEntity<>(response, status);
    }
}
