package com.example.authorization.exception.handler;

import com.example.authorization.annotation.AuthControllerExceptionHandler;
import com.example.template.model.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(annotations = AuthControllerExceptionHandler.class)
public class ExceptionHandlerAuth {
    private final String path = "/auth";

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), path);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException e) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.CONFLICT.value(), e.getMessage(), path);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
