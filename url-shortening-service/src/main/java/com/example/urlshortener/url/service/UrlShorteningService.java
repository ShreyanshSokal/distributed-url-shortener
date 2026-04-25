package com.example.urlshortener.url.service;

import com.example.urlshortener.common.model.ResolvedUrlPayload;
import com.example.urlshortener.url.config.AppProperties;
import com.example.urlshortener.url.domain.UrlMapping;
import com.example.urlshortener.url.repository.UrlMappingRepository;
import com.example.urlshortener.url.web.CreateShortUrlRequest;
import com.example.urlshortener.url.web.ShortUrlResponse;
import com.example.urlshortener.url.web.UrlMappingResponse;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UrlShorteningService {

    private final UrlMappingRepository repository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final SnowflakeIdGenerator idGenerator;
    private final UrlHashService hashService;
    private final AppProperties properties;

    public UrlShorteningService(
            UrlMappingRepository repository,
            ShortCodeGenerator shortCodeGenerator,
            SnowflakeIdGenerator idGenerator,
            UrlHashService hashService,
            AppProperties properties
    ) {
        this.repository = repository;
        this.shortCodeGenerator = shortCodeGenerator;
        this.idGenerator = idGenerator;
        this.hashService = hashService;
        this.properties = properties;
    }

    @Transactional
    public ShortUrlResponse createShortUrl(CreateShortUrlRequest request) {
        String requestHash = hashService.createRequestHash(request.longUrl(), request.customAlias(), request.expiryAt());
        return repository.findByRequestHash(requestHash)
                .map(this::toShortUrlResponse)
                .orElseGet(() -> persistNewMapping(request, requestHash));
    }

    public UrlMappingResponse getMapping(String shortCode) {
        UrlMapping mapping = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));
        return UrlMappingResponse.from(mapping);
    }

    public ResolvedUrlPayload resolveActiveUrl(String shortCode) {
        UrlMapping mapping = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));
        boolean active = !mapping.isExpired();
        return new ResolvedUrlPayload(mapping.getShortCode(), mapping.getLongUrl(), mapping.getExpiryAt(), active);
    }

    private ShortUrlResponse persistNewMapping(CreateShortUrlRequest request, String requestHash) {
        String shortCode = request.customAlias() == null || request.customAlias().isBlank()
                ? nextUniqueShortCode()
                : request.customAlias();

        if (repository.existsByShortCode(shortCode)) {
            throw new AliasAlreadyExistsException(shortCode);
        }

        UrlMapping mapping = new UrlMapping(
                idGenerator.nextId(),
                shortCode,
                request.longUrl(),
                requestHash,
                Instant.now(),
                request.expiryAt()
        );

        try {
            return toShortUrlResponse(repository.save(mapping));
        } catch (DataIntegrityViolationException ex) {
            throw new AliasAlreadyExistsException(shortCode, ex);
        }
    }

    private String nextUniqueShortCode() {
        String candidate;
        do {
            candidate = shortCodeGenerator.generate();
        } while (repository.existsByShortCode(candidate));
        return candidate;
    }

    private ShortUrlResponse toShortUrlResponse(UrlMapping mapping) {
        return new ShortUrlResponse(
                mapping.getShortCode(),
                properties.baseUrl() + "/" + mapping.getShortCode(),
                mapping.getLongUrl(),
                mapping.getCreatedAt(),
                mapping.getExpiryAt()
        );
    }
}
