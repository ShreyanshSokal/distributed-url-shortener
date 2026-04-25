package com.example.urlshortener.redirect.service;

public class RedirectUnavailableException extends RuntimeException {

    public RedirectUnavailableException(String shortCode) {
        super("Short code is expired or unavailable: " + shortCode);
    }
}
