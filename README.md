# ChallengeForoHub

API REST para gestión de tópicos desarrollada con Spring Boot, JPA, Flyway y MySQL.

## Funcionalidades implementadas

- Registro de tópico: `POST /topicos`
- Listado de tópicos: `GET /topicos`
- Detalle de tópico por ID: `GET /topicos/{id}`
- Actualización de tópico: `PUT /topicos/{id}`
- Eliminación de tópico: `DELETE /topicos/{id}`

## Reglas de negocio

- Todos los campos de entrada son obligatorios (`titulo`, `mensaje`, `autor`, `curso`).
- No se permiten tópicos duplicados con el mismo `titulo` y `mensaje`.
- El `id` debe ser válido y positivo en endpoints por ID.
- En operaciones por ID:
  - si no existe, responde `404 Not Found`;
  - en eliminación exitosa, responde `204 No Content`;
  - en duplicados, responde `409 Conflict`.

## Tecnologías

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Validation
- Flyway
- MySQL
- Maven

## Base de datos y migraciones

El proyecto usa MySQL y Flyway con migraciones en:

- `src/main/resources/db/migration/V1__create-table-topicos.sql`
- `src/main/resources/db/migration/V2__add-unique-constraint-topicos-titulo-mensaje.sql`

Configuración en:

- `src/main/resources/application.properties`

## Ejecución local

1. Crear la base de datos `forohub` en MySQL.
2. Ejecutar:

```bash
./mvnw spring-boot:run
```

## Pruebas rápidas de endpoints

Base URL:

- `http://localhost:8080`

Endpoints:

- `POST /topicos`
- `GET /topicos`
- `GET /topicos/{id}`
- `PUT /topicos/{id}`
- `DELETE /topicos/{id}`

## Comandos útiles

Ejecutar tests:

```bash
./mvnw test
```
