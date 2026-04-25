package com.example.urlshortener.analytics.repository;

import com.example.urlshortener.analytics.domain.UrlAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlAnalyticsRepository extends JpaRepository<UrlAnalytics, String> {
}
