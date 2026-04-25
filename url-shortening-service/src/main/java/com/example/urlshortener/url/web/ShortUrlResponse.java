package com.example.urlshortener.url.web;

import java.time.Instant;

public record ShortUrlResponse(
        String shortCode,
        String shortUrl,
        String longUrl,
        Instant createdAt,
        Instant expiryAt
) {
}
