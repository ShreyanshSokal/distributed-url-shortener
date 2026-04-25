package com.example.urlshortener.url.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;

public record CreateShortUrlRequest(
        @NotBlank(message = "longUrl is required")
        @Pattern(
                regexp = "^(https?)://.+$",
                message = "Only HTTP/HTTPS URLs are supported"
        )
        String longUrl,
        @Pattern(
                regexp = "^[a-zA-Z0-9_-]{0,32}$",
                message = "customAlias can contain only letters, digits, underscore, and hyphen"
        )
        String customAlias,
        Instant expiryAt
) {
}
