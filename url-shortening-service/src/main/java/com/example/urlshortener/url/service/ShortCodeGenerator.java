package com.example.urlshortener.url.service;

import com.example.urlshortener.url.config.AppProperties;
import org.springframework.stereotype.Component;

@Component
public class ShortCodeGenerator {

    private final SnowflakeIdGenerator idGenerator;
    private final Base62Encoder encoder;
    private final AppProperties properties;

    public ShortCodeGenerator(SnowflakeIdGenerator idGenerator, Base62Encoder encoder, AppProperties properties) {
        this.idGenerator = idGenerator;
        this.encoder = encoder;
        this.properties = properties;
    }

    public String generate() {
        String encoded = encoder.encode(idGenerator.nextId());
        if (encoded.length() >= properties.shortCodeLength()) {
            return encoded.substring(encoded.length() - properties.shortCodeLength());
        }
        return "0".repeat(properties.shortCodeLength() - encoded.length()) + encoded;
    }
}
