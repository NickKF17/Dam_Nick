# API de Usuarios

## Descripción

Endpoint REST `/api/v1/usuarios` para gestionar usuarios.

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/v1/usuarios` | Lista los usuarios. |
| `GET` | `/api/v1/usuarios/{id}` | Obtiene un usuario. |
| `POST` | `/api/v1/usuarios` | Crea un usuario. |
| `PUT` | `/api/v1/usuarios/{id}` | Actualiza un usuario. |
| `DELETE` | `/api/v1/usuarios/{id}` | Elimina un usuario. |

## Parámetros

| Nombre | Ubicación | Tipo | Obligatorio | Descripción |
|--------|-----------|------|-------------|-------------|
| `id` | Ruta | integer | Sí (por id) | Identificador del usuario. |
| `nombre` | Cuerpo JSON | string | Sí (`POST`, `PUT`) | Nombre completo. |
| `email` | Cuerpo JSON | string | Sí (`POST`, `PUT`) | Correo electrónico válido. |
| `password` | Cuerpo JSON | string | Sí (`POST`) | Contraseña (mínimo 8 caracteres). |

## Respuestas posibles

| Código | Descripción |
|--------|-------------|
| `200 OK` | Operación realizada correctamente. |
| `400 Bad Request` | Datos inválidos o campos obligatorios ausentes. |
| `404 Not Found` | No existe el usuario indicado. |
| `500 Internal Server Error` | Error inesperado del servidor. |

## Ejemplo de solicitud

`POST /api/v1/usuarios`

```json
{
  "nombre": "Laura Gómez",
  "email": "laura.gomez@ejemplo.com",
  "password": "Segura#2026"
}
```

## Ejemplo de respuesta

```json
{
  "id": 42,
  "nombre": "Laura Gómez",
  "email": "laura.gomez@ejemplo.com"
}
```
