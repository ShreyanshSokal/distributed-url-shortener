package com.example.urlshortener.common.model;

import java.io.Serializable;
import java.time.Instant;

public record ResolvedUrlPayload(
        String shortCode,
        String longUrl,
        Instant expiryAt,
        boolean active
) implements Serializable {
}
