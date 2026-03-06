# ChallengeForoHub

API REST para gestion de topicos desarrollada con Spring Boot.

## Descripcion

Este proyecto implementa el challenge de ForoHub con operaciones CRUD de topicos, validaciones de negocio, persistencia en MySQL con Flyway, autenticacion de usuarios y control de acceso con JWT.

## Funcionalidades

- Registrar topico: `POST /topicos`
- Listar topicos: `GET /topicos`
- Ver detalle de topico: `GET /topicos/{id}`
- Actualizar topico: `PUT /topicos/{id}`
- Eliminar topico: `DELETE /topicos/{id}`
- Iniciar sesion y obtener token JWT: `POST /login`

## Reglas de negocio implementadas

- Campos obligatorios en alta y actualizacion: `titulo`, `mensaje`, `autor`, `curso`.
- No se permiten topicos duplicados con mismo `titulo` y `mensaje`.
- En endpoints por ID, el `id` debe ser positivo.
- Si el recurso no existe se responde `404`.
- En eliminacion exitosa se responde `204`.
- Si hay conflicto por duplicado se responde `409`.
- Los endpoints de topicos requieren JWT valido.

## Arquitectura y seguridad

- `SecurityConfigurations`: configura acceso stateless con Spring Security.
- `AutenticacionController`: recibe credenciales y devuelve JWT.
- `TokenService`: genera y valida tokens JWT.
- `SecurityFilter`: valida token Bearer en cada solicitud protegida.
- `AutenticacionService`: carga usuarios desde base de datos.

## Tecnologias

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Security
- JWT (Auth0 `java-jwt`)
- Flyway
- MySQL
- Maven

## Base de datos y migraciones

Migraciones SQL:

- `V1__create-table-topicos.sql`
- `V2__add-unique-constraint-topicos-titulo-mensaje.sql`
- `V3__create-table-usuarios.sql`

Tablas principales:

- `topicos`
- `usuarios`

Usuario inicial para pruebas:

- `login`: `admin`
- `clave`: `admin123`

## Configuracion

Archivo: `src/main/resources/application.properties`

Propiedades relevantes:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `jwt.secret`
- `jwt.expiration`

## Ejecucion local

1. Crear la base de datos `forohub` en MySQL.
2. Levantar la API:

```bash
./mvnw spring-boot:run
```

## Uso de autenticacion JWT

1. Solicitar token:

`POST http://localhost:8080/login`

Body:

```json
{
  "login": "admin",
  "clave": "admin123"
}
```

2. Enviar token en endpoints protegidos:

Header:

`Authorization: Bearer <token>`

## Endpoints

Base URL: `http://localhost:8080`

- `POST /login`
- `POST /topicos`
- `GET /topicos`
- `GET /topicos/{id}`
- `PUT /topicos/{id}`
- `DELETE /topicos/{id}`

## Pruebas en Postman o Insomnia

Flujo recomendado:

1. Hacer login y copiar token.
2. Crear un topico.
3. Listar topicos.
4. Consultar detalle por ID.
5. Actualizar topico.
6. Eliminar topico.
7. Verificar `404` al consultar ID eliminado.

## Comandos utiles

Ejecutar tests:

```bash
./mvnw test
```
