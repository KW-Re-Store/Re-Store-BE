# Re-Store BE

Spring Boot API for the Re-Store commercial-area policy dashboard.

## Requirements

- Java 21
- Maven 3.9+

## Start

```bash
mvn spring-boot:run
```

The API runs on port `4000` by default. You can override it with `PORT`.

```bash
PORT=8080 mvn spring-boot:run
```

For frontend CORS, set comma-separated origins with `CORS_ALLOWED_ORIGINS`.

```bash
CORS_ALLOWED_ORIGINS=http://localhost:5173 mvn spring-boot:run
```
