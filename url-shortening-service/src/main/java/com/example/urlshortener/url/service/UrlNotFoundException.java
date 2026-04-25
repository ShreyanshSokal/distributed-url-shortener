package com.example.urlshortener.url.service;

public class UrlNotFoundException extends RuntimeException {

    public UrlNotFoundException(String shortCode) {
        super("Short code not found: " + shortCode);
    }
}
