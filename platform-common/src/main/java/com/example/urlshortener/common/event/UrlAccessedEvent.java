package com.example.urlshortener.common.event;

import java.io.Serializable;
import java.time.Instant;

public record UrlAccessedEvent(
        String shortCode,
        String longUrl,
        Instant accessedAt,
        String clientIp,
        String userAgent
) implements Serializable {
}
