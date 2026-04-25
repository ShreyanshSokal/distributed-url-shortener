package com.example.urlshortener.url.service;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

@Component
public class UrlHashService {

    public String createRequestHash(String longUrl, String customAlias, Instant expiryAt) {
        String payload = longUrl + "|" + normalize(customAlias) + "|" + normalize(expiryAt);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 not available", ex);
        }
    }

    private String normalize(Object value) {
        return value == null ? "" : value.toString();
    }
}
