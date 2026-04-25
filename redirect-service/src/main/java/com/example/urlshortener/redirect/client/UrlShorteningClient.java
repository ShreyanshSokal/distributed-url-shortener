package com.example.urlshortener.redirect.client;

import com.example.urlshortener.common.model.ResolvedUrlPayload;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "url-shortening-service")
public interface UrlShorteningClient {

    @GetMapping("/api/v1/urls/internal/{shortCode}")
    @CircuitBreaker(name = "urlShorteningClient")
    ResolvedUrlPayload resolve(@PathVariable("shortCode") String shortCode);
}
