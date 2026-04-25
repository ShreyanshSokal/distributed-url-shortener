package com.example.urlshortener.analytics.web;

import java.time.Instant;

public record AnalyticsResponse(
        String shortCode,
        long clickCount,
        Instant lastAccessedAt
) {
}
