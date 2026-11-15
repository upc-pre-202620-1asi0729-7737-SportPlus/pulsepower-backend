# PulsePower Backend

RESTful API de PulsePower desarrollada con Java 27 y Spring Boot 4.1.2, con persistencia en PostgreSQL y arquitectura hexagonal (screaming architecture).

## ¿Qué hace?

- Autenticación y autorización con JWT (BCrypt + HS256) — bounded context **IAM**.
- Registro y consulta de sesiones de entrenamiento y plan de actividades — **Training**.
- Registro de sueño con validación de solapamientos y rutina de descanso — **Sleep**.
- Check-ins diarios de bienestar, hábitos y registros de cumplimiento — **Wellness**.
- Racha, logros y progreso compartido — **Community**.
- Resumen fisiológico (HRV, recuperación, alertas) y asistente de IA — **Physiology**.
- Generación de informes de progreso y exportación PDF — **Reports**.

El asistente de IA (`POST /api/v1/assistant/messages`) interpreta los registros del usuario con Gemini y devuelve recomendaciones de sueño, entrenamiento y bienestar. El endpoint `POST /api/v1/assistant/routines` genera una rutina de entrenamiento personalizada y la guarda en el plan del usuario.

## Requisitos

- Java 27
- Maven 3.9+
- PostgreSQL 16+ (o variable de entorno `DB_URL` apuntando a una instancia)
- Variable `GEMINI_API_KEY` con una clave válida de Google AI Studio (opcional; sin ella el asistente responde en modo fallback local)

## Cómo ejecutar en desarrollo

```bash
export JWT_SECRET=local-dev-secret-min-32-chars
export GEMINI_API_KEY=   # opcional
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080/api/v1`.  
Swagger UI: `http://localhost:8080/swagger-ui.html`  
Documento OpenAPI: `http://localhost:8080/v3/api-docs`

## Pruebas

```bash
./mvnw test
```

## Despliegue en producción (Render)

El proyecto incluye un `Dockerfile` multi-stage. Render construye la imagen en cada commit integrado en `main`. Variables de entorno requeridas:

| Variable | Descripción |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `DB_URL` | URL JDBC de PostgreSQL (`jdbc:postgresql://…`) |
| `DB_USERNAME` | Usuario de la base de datos |
| `DB_PASSWORD` | Contraseña de la base de datos |
| `JWT_SECRET` | Secreto HS256 (mínimo 32 caracteres) |
| `CORS_ALLOWED_ORIGINS` | Origen del frontend (ej. `https://pulsepower-webapp.vercel.app`) |
| `GEMINI_API_KEY` | Clave de Google AI Studio para el asistente |

**URL de producción:** https://pulsepower-backend.onrender.com/api/v1

## Tecnologías

- Java 27 · Spring Boot 4.1.2 · Spring Data JPA · Spring Security
- PostgreSQL · Flyway (migraciones) · Hibernate
- SpringDoc (OpenAPI/Swagger)
- Google Gemini API (`gemini-2.5-flash-lite`) vía `RestClient`
- JUnit 5 · Mockito

## Organización del código

```
src/main/java/com/sportplus/pulsepower/
├── iam/           # Autenticación, usuarios y suscripciones
├── training/      # Sesiones y plan de entrenamiento
├── sleep/         # Registros y rutina de sueño
├── wellness/      # Check-ins, hábitos y registros
├── community/     # Racha, logros y progreso compartido
├── physiology/    # Resumen fisiológico, asistente IA y rutinas
├── reports/       # Informes y entradas de historial
└── shared/        # Infraestructura transversal (JPA, excepciones, seguridad)
```

Cada bounded context se divide en las capas `domain`, `application`, `infrastructure` e `interfaces`.
