# URL Shortener Microservices Platform

This project implements a scalable URL shortening platform using Java 17, Spring Boot, and Spring Cloud microservices. The design favors low-latency redirects, asynchronous analytics, cache-first hot path handling, and horizontal scale-out.

## Services

- `discovery-service`: Eureka service registry for dynamic discovery and client-side load balancing.
- `config-server`: Centralized configuration using Spring Cloud Config with a native in-repo config repository.
- `api-gateway`: Single public entry point with routing, Redis-backed rate limiting, and circuit breaker fallback.
- `url-shortening-service`: Owns URL creation, idempotency, persistence, expiry checks, and internal resolution APIs.
- `redirect-service`: Handles the read-heavy redirect path with Redis cache-aside and Kafka event publishing.
- `analytics-service`: Consumes redirect events asynchronously and exposes aggregated analytics.
- `platform-common`: Shared event and payload contracts used across services.

## Key Design Decisions

- **Idempotent create**: `url-shortening-service` computes a deterministic SHA-256 request hash from `longUrl + customAlias + expiryAt`, then returns an existing mapping when the same request is replayed.
- **Distributed ID generation**: a Snowflake-style ID generator avoids database sequence contention under high write throughput.
- **Short code strategy**: Base62-encode the generated ID and keep the hot path deterministic. Custom aliases are supported with uniqueness checks.
- **Low-latency redirects**: `redirect-service` uses Redis cache-aside so frequently accessed URLs avoid a database hit and can remain below the latency target.
- **Async analytics**: redirect traffic emits `UrlAccessedEvent` to Kafka so the redirect response is not blocked by writes to analytics storage.
- **Fault tolerance**: gateway and Feign clients use circuit breaker support, while service discovery enables horizontal scaling and rolling restarts.

## API Overview

- `POST /shorten`
- `GET /{shortCode}`
- `GET /analytics/{shortCode}`

Internal service APIs still use `/api/v1/...` paths behind the gateway so service-to-service traffic stays namespaced.

Example request:

```json
{
  "longUrl": "https://example.com/very/long/path",
  "customAlias": "launch-2026",
  "expiryAt": "2026-12-31T23:59:59Z"
}
```

## Data Model

Primary URL mapping table:

- `id` bigint primary key
- `short_code` varchar unique indexed
- `long_url` varchar
- `request_hash` varchar unique indexed
- `created_at` timestamp
- `expiry_at` timestamp nullable

Analytics table:

- `short_code` primary key
- `click_count`
- `last_accessed_at`

## Scaling Notes

- Shard `url_mapping` by hash of `short_code` once a single primary becomes a bottleneck.
- Add read replicas for reporting and back-office queries; keep redirects on cache + primary-owned read APIs.
- Put CDN or global edge caching in front of the gateway for geographic latency reduction.
- Rate limit at the gateway to reduce abuse and bot amplification.
- Keep `short_code` immutable so cache keys remain stable and easy to partition.

## Local Run

1. Build the jars:

```bash
mvn clean package -DskipTests
```

2. Start the full stack:

```bash
docker compose up -d
```

3. If you prefer running services directly instead of containers, start them in this order:

```bash
mvn -pl discovery-service spring-boot:run
mvn -pl config-server spring-boot:run
mvn -pl url-shortening-service spring-boot:run
mvn -pl redirect-service spring-boot:run
mvn -pl analytics-service spring-boot:run
mvn -pl api-gateway spring-boot:run
```

## Production Hardening Ideas

- Add auth and quota enforcement for tenant-isolated usage.
- Persist full clickstream events in object storage or OLAP for richer analytics.
- Replace `ddl-auto=update` with Flyway or Liquibase migrations.
- Add contract tests and Testcontainers for infrastructure-backed integration tests.
- Externalize the config repository to Git for independent config rollout.
