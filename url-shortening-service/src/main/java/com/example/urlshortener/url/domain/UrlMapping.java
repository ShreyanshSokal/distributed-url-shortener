package com.example.urlshortener.url.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "url_mapping", indexes = {
        @Index(name = "idx_url_mapping_short_code", columnList = "short_code", unique = true),
        @Index(name = "idx_url_mapping_request_hash", columnList = "request_hash", unique = true)
})
public class UrlMapping {

    @Id
    private Long id;

    @Column(name = "short_code", nullable = false, unique = true, length = 32)
    private String shortCode;

    @Column(name = "long_url", nullable = false, length = 2048)
    private String longUrl;

    @Column(name = "request_hash", nullable = false, unique = true, length = 64)
    private String requestHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expiry_at")
    private Instant expiryAt;

    protected UrlMapping() {
    }

    public UrlMapping(Long id, String shortCode, String longUrl, String requestHash, Instant createdAt, Instant expiryAt) {
        this.id = id;
        this.shortCode = shortCode;
        this.longUrl = longUrl;
        this.requestHash = requestHash;
        this.createdAt = createdAt;
        this.expiryAt = expiryAt;
    }

    public Long getId() {
        return id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public String getRequestHash() {
        return requestHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiryAt() {
        return expiryAt;
    }

    public boolean isExpired() {
        return expiryAt != null && expiryAt.isBefore(Instant.now());
    }
}
