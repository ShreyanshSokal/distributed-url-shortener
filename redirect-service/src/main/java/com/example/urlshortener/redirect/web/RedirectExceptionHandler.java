package com.example.urlshortener.redirect.web;

import com.example.urlshortener.redirect.service.RedirectUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class RedirectExceptionHandler {

    @ExceptionHandler(RedirectUnavailableException.class)
    public ResponseEntity<Map<String, String>> handleRedirectUnavailable(RedirectUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(Map.of("message", ex.getMessage()));
    }
}
