package com.example.urlshortener.redirect.service;

import com.example.urlshortener.common.event.UrlAccessedEvent;
import com.example.urlshortener.common.model.ResolvedUrlPayload;
import com.example.urlshortener.redirect.client.UrlShorteningClient;
import com.example.urlshortener.redirect.config.AppCacheProperties;
import com.example.urlshortener.redirect.config.AppTopicProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class RedirectService {

    private static final String CACHE_PREFIX = "redirect:url:";

    private final UrlShorteningClient urlShorteningClient;
    private final RedisTemplate<String, ResolvedUrlPayload> redisTemplate;
    private final KafkaTemplate<String, UrlAccessedEvent> kafkaTemplate;
    private final AppCacheProperties cacheProperties;
    private final AppTopicProperties topicProperties;

    public RedirectService(
            UrlShorteningClient urlShorteningClient,
            RedisTemplate<String, ResolvedUrlPayload> redisTemplate,
            KafkaTemplate<String, UrlAccessedEvent> kafkaTemplate,
            AppCacheProperties cacheProperties,
            AppTopicProperties topicProperties
    ) {
        this.urlShorteningClient = urlShorteningClient;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.cacheProperties = cacheProperties;
        this.topicProperties = topicProperties;
    }

    public String resolveTargetUrl(String shortCode, String clientIp, String userAgent) {
        String cacheKey = CACHE_PREFIX + shortCode;
        ResolvedUrlPayload payload = redisTemplate.opsForValue().get(cacheKey);
        if (payload == null) {
            payload = urlShorteningClient.resolve(shortCode);
            // Cache-aside keeps redirect latency low without coupling persistence to the edge service.
            redisTemplate.opsForValue().set(cacheKey, payload, Duration.ofSeconds(cacheProperties.urlTtlSeconds()));
        }
        if (!payload.active()) {
            throw new RedirectUnavailableException(shortCode);
        }
        kafkaTemplate.send(topicProperties.urlAccessed(), shortCode, new UrlAccessedEvent(
                shortCode,
                payload.longUrl(),
                Instant.now(),
                clientIp,
                userAgent
        ));
        return payload.longUrl();
    }
}
