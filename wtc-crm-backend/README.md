# WTC CRM Backend - Sprint 2

Spring Boot 3 API for WTC CRM platform.

## Prerequisites

- Java 17
- Maven 3.9+
- Firebase service account (optional for local bootstrap)

## Run

```bash
mvn spring-boot:run
```

## Main URLs

- API docs: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Authentication seed

- Default operator:
  - email: `operator@wtc.com`
  - password: `123456`

## Environment notes

- `src/main/resources/firebase-service-account.json` should contain your Firebase Admin SDK credentials.
- If credentials are not present, app still starts, but Firebase-integrated operations are mocked/logged.

## Implemented modules

- Auth + JWT
- Clients + Segments
- Chat (REST + WebSocket STOMP)
- Campaigns
- Tasks
- Profile360 aggregation
- Media validation/upload URL generation
- Audit aspect infrastructure
