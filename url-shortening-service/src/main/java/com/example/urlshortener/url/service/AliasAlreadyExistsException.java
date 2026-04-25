package com.example.urlshortener.url.service;

public class AliasAlreadyExistsException extends RuntimeException {

    public AliasAlreadyExistsException(String alias) {
        super("Short code already exists: " + alias);
    }

    public AliasAlreadyExistsException(String alias, Throwable cause) {
        super("Short code already exists: " + alias, cause);
    }
}
