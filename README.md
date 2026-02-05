# SubHub

[![CI](https://github.com/ymc3/subhub/actions/workflows/ci.yml/badge.svg)](https://github.com/ymc3/subhub/actions/workflows/ci.yml)

Minimal subscription backend (portfolio project). Built with **Java 17 + Spring Boot 3**.

## Quick start

```bash
cd /home/mingc/projects/subhub/subhub
mvn test
mvn spring-boot:run
```

## Health check

```bash
curl http://127.0.0.1:8080/health
```

Example response:

```json
{"status":"ok","service":"subhub"}
```

## API

### Create user

```bash
curl -sS -X POST http://127.0.0.1:8080/users \
  -H 'Content-Type: application/json' \
  -d '{"name":"Alice","email":"alice@example.com"}'
```

### Get user

```bash
curl -sS http://127.0.0.1:8080/users/1001
```

### Create subscription (defaults to TRIAL)

```bash
curl -sS -X POST http://127.0.0.1:8080/subscriptions \
  -H 'Content-Type: application/json' \
  -d '{"userId":1001,"plan":"basic"}'
```

### Get subscription

```bash
curl -sS http://127.0.0.1:8080/subscriptions/5001
```

## Notes
- Storage is in-memory for now (no database yet). Restarting the app resets data.
