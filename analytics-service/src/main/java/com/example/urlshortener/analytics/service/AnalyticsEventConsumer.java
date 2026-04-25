package com.example.urlshortener.analytics.service;

import com.example.urlshortener.common.event.UrlAccessedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventConsumer {

    private final AnalyticsService analyticsService;

    public AnalyticsEventConsumer(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @KafkaListener(topics = "${app.topics.url-accessed}", groupId = "analytics-service")
    public void handleUrlAccessed(UrlAccessedEvent event) {
        analyticsService.recordAccess(event);
    }
}
