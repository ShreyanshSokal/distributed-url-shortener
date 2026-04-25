package com.example.urlshortener.url.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        String baseUrl,
        int shortCodeLength,
        SnowflakeProperties snowflake,
        TopicProperties topics
) {
    public record SnowflakeProperties(long workerId, long datacenterId) {
    }

    public record TopicProperties(String urlAccessed) {
    }
}
