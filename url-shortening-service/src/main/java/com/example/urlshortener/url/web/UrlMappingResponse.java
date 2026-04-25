package com.example.urlshortener.url.web;

import com.example.urlshortener.url.domain.UrlMapping;

import java.time.Instant;

public record UrlMappingResponse(
        String shortCode,
        String longUrl,
        Instant createdAt,
        Instant expiryAt,
        boolean expired
) {
    public static UrlMappingResponse from(UrlMapping mapping) {
        return new UrlMappingResponse(
                mapping.getShortCode(),
                mapping.getLongUrl(),
                mapping.getCreatedAt(),
                mapping.getExpiryAt(),
                mapping.isExpired()
        );
    }
}
