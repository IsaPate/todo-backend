# Todoboard API

A REST API for a Kanban-style todo board. Users register, log in with JWT, and manage their own boards and cards. Each user can only see and change their own data.

## Tech stack

- **Java 21**
- **Spring Boot 4.1** (Web MVC, Data JPA, Validation, DevTools)
- **Spring Security 7** with stateless **JWT** authentication ([jjwt](https://github.com/jwtk/jjwt) 0.12)
- **PostgreSQL** with **Hibernate**
- **BCrypt** password hashing
- **Lombok**
- **Maven** (wrapper included)

## Features

- User registration and login. Login returns a JWT token.
- JWT filter validates the `Authorization: Bearer <token>` header on every request.
- Role-based access (`USER`, `ADMIN`).
- Boards that contain ordered cards (`TODO`, `IN_PROGRESS`, `DONE`).
- Ownership checks: users can only access their own boards and cards.
- Request validation with clear `400` errors.
- DTOs for requests and responses, so entities are never exposed.
- CORS configured for a local frontend (`http://localhost:5173`).

## Data model

```text
User 1 ── * Board 1 ── * Card
```

## API endpoints

Every endpoint except register and login needs `Authorization: Bearer <token>`.

### Auth

| Method | Endpoint             | Description                   |
|--------|----------------------|-------------------------------|
| POST   | `/api/auth/register` | Create an account             |
| POST   | `/api/auth/login`    | Log in and receive a JWT token |

### Boards

| Method | Endpoint          | Description                       |
|--------|-------------------|-----------------------------------|
| GET    | `/api/boards`     | List the current user's boards    |
| GET    | `/api/board/{id}` | Get one board with its cards      |
| POST   | `/api/board`      | Create a board                    |
| PUT    | `/api/board/{id}` | Update a board                    |
| DELETE | `/api/board/{id}` | Delete a board                    |

### Cards

| Method | Endpoint         | Description                     |
|--------|------------------|---------------------------------|
| GET    | `/api/cards`     | List the current user's cards   |
| GET    | `/api/card/{id}` | Get one card                    |
| POST   | `/api/card`      | Create a card in a board        |
| PUT    | `/api/card/{id}` | Update a card                   |
| DELETE | `/api/card/{id}` | Delete a card                   |

### Example requests

Register or log in:

```json
{ "username": "random_username", "password": "secret123" }
```

Create a card:

```json
{
  "title": "Write README",
  "description": "Document the API",
  "status": "TODO",
  "position": 1,
  "boardId": 1
}
```

## Getting started

### Prerequisites

- Java 21
- PostgreSQL running locally

### 1. Create the database

```sql
CREATE DATABASE random_database;
```

### 2. Configure environment variables

Create a `.env` file in the project root. It is ignored by git.

```properties
DATABASE_URL=jdbc:postgresql://localhost:5432/random_database
DATABASE_USERNAME=your_db_user
DATABASE_PASSWORD=your_db_password

# Base64-encoded key, at least 256 bits
JWT_SECRET=your_base64_secret
# Token lifetime in milliseconds (1800000 = 30 minutes)
JWT_EXPIRATION=1800000
```

To generate a secret:

```bash
openssl rand -base64 32
```

### 3. Run

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`. Hibernate creates the tables on first run.
