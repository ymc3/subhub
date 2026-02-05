# SubHub

[![CI](https://github.com/ymc3/subhub/actions/workflows/ci.yml/badge.svg)](https://github.com/ymc3/subhub/actions/workflows/ci.yml)

Minimal subscription backend (portfolio project). Built with **Java 17 + Spring Boot 3**.

## Quick start

Start Postgres:

```bash
cd /home/mingc/projects/subhub/subhub
docker compose up -d
```

Run the app:

```bash
mvn test
mvn spring-boot:run
```

## Health check

```bash
curl http://127.0.0.1:8080/api/health
```

Example response:

```json
{"status":"ok","service":"subhub"}
```

## API

### Create user

```bash
curl -sS -X POST http://127.0.0.1:8080/api/users \
  -H 'Content-Type: application/json' \
  -d '{"name":"Alice","email":"alice@example.com"}'
```

### Get user

```bash
curl -sS http://127.0.0.1:8080/api/users/1
```

### Get user with subscriptions

```bash
curl -sS http://127.0.0.1:8080/api/users/1/with-subscriptions
```

### Create subscription (defaults to TRIAL)

```bash
curl -sS -X POST http://127.0.0.1:8080/api/subscriptions \
  -H 'Content-Type: application/json' \
  -d '{"userId":1001,"plan":"basic"}'
```

### Get subscription

```bash
curl -sS http://127.0.0.1:8080/api/subscriptions/1
```

### List subscriptions (paged)

All subscriptions:

```bash
curl -sS 'http://127.0.0.1:8080/api/subscriptions?page=0&size=20'
```

By user:

```bash
curl -sS 'http://127.0.0.1:8080/api/subscriptions?userId=1&page=0&size=20'
```

By status:

```bash
curl -sS 'http://127.0.0.1:8080/api/subscriptions?status=ACTIVE&page=0&size=20'
```

By user + status:

```bash
curl -sS 'http://127.0.0.1:8080/api/subscriptions?userId=1&status=ACTIVE&page=0&size=20'
```

Response shape:

```json
{
  "items": [],
  "page": 0,
  "size": 20,
  "totalItems": 0,
  "totalPages": 0
}
```

### Update subscription status

Option A (generic):

```bash
curl -sS -X PUT http://127.0.0.1:8080/api/subscriptions/1 \
  -H 'Content-Type: application/json' \
  -d '{"status":"ACTIVE"}'
```

Option B (actions):

```bash
curl -sS -X POST http://127.0.0.1:8080/api/subscriptions/1/activate
curl -sS -X POST http://127.0.0.1:8080/api/subscriptions/1/cancel
```

## Notes
- Storage is in-memory for now (no database yet). Restarting the app resets data.
