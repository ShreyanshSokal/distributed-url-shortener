package com.example.urlshortener.url.web;

import com.example.urlshortener.common.model.ResolvedUrlPayload;
import com.example.urlshortener.url.service.UrlShorteningService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlShorteningController {

    private final UrlShorteningService service;

    public UrlShorteningController(UrlShorteningService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortUrlResponse> createShortUrl(@Valid @RequestBody CreateShortUrlRequest request) {
        return ResponseEntity.ok(service.createShortUrl(request));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<UrlMappingResponse> getMapping(@PathVariable String shortCode) {
        return ResponseEntity.ok(service.getMapping(shortCode));
    }

    @GetMapping("/internal/{shortCode}")
    public ResponseEntity<ResolvedUrlPayload> resolveActiveUrl(@PathVariable String shortCode) {
        return ResponseEntity.ok(service.resolveActiveUrl(shortCode));
    }
}
