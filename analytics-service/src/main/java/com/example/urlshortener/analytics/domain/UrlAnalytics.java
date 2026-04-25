package com.example.urlshortener.analytics.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "url_analytics")
public class UrlAnalytics {

    @Id
    @Column(name = "short_code", nullable = false, length = 32)
    private String shortCode;

    @Column(name = "click_count", nullable = false)
    private long clickCount;

    @Column(name = "last_accessed_at")
    private Instant lastAccessedAt;

    protected UrlAnalytics() {
    }

    public UrlAnalytics(String shortCode, long clickCount, Instant lastAccessedAt) {
        this.shortCode = shortCode;
        this.clickCount = clickCount;
        this.lastAccessedAt = lastAccessedAt;
    }

    public String getShortCode() {
        return shortCode;
    }

    public long getClickCount() {
        return clickCount;
    }

    public Instant getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void increment(Instant accessedAt) {
        this.clickCount++;
        this.lastAccessedAt = accessedAt;
    }
}
