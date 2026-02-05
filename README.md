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

## Notes
- This is v0 scaffold. Next: Users + Subscriptions endpoints.
