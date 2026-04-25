package com.example.urlshortener.url.repository;

import com.example.urlshortener.url.domain.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByShortCode(String shortCode);

    Optional<UrlMapping> findByRequestHash(String requestHash);

    boolean existsByShortCode(String shortCode);
}
