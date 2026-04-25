package com.example.urlshortener.redirect.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.cache")
public record AppCacheProperties(long urlTtlSeconds) {
}
