package com.example.urlshortener.analytics.service;

import com.example.urlshortener.analytics.domain.UrlAnalytics;
import com.example.urlshortener.analytics.repository.UrlAnalyticsRepository;
import com.example.urlshortener.analytics.web.AnalyticsResponse;
import com.example.urlshortener.common.event.UrlAccessedEvent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private final UrlAnalyticsRepository repository;

    public AnalyticsService(UrlAnalyticsRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void recordAccess(UrlAccessedEvent event) {
        UrlAnalytics analytics = repository.findById(event.shortCode())
                .orElseGet(() -> new UrlAnalytics(event.shortCode(), 0, event.accessedAt()));
        analytics.increment(event.accessedAt());
        repository.save(analytics);
    }

    public AnalyticsResponse getAnalytics(String shortCode) {
        UrlAnalytics analytics = repository.findById(shortCode)
                .orElse(new UrlAnalytics(shortCode, 0, null));
        return new AnalyticsResponse(
                analytics.getShortCode(),
                analytics.getClickCount(),
                analytics.getLastAccessedAt()
        );
    }
}
