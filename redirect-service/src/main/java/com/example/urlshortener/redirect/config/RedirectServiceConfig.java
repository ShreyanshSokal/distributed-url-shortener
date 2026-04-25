package com.example.urlshortener.redirect.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AppCacheProperties.class, AppTopicProperties.class})
public class RedirectServiceConfig {
}
