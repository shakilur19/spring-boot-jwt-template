# Spring Boot JWT Template

A production-ready starter template for building secure authentication APIs with **Spring Boot**, **Spring Security**, **JWT**, and **MySQL**.  
This project provides a solid base for implementing user registration, login, JWT-based authorization, refresh-token, profile access, and password management.

---

## Features

- User signup with validation
- User login with JWT access token and refresh token
- JWT-based request authorization
- Protected profile endpoint
- Password reset endpoint
- Request/response DTO structure
- Global exception handling with `@RestControllerAdvice`
- Custom Validator `@ValidPassword`
- MySQL integration with Spring Data JPA
- Docker Compose setup for local development
- Environment-based configuration using `.env.dev` and `.env.prod`
- IntelliJ-friendly development workflow

---

## Tech Stack

- **Java 21**
- **Spring Boot 4**
- **Spring Security**
- **JWT (JJWT)**
- **Spring Data JPA**
- **MySQL**
- **Docker / Docker Compose**
- **Lombok**
- **Maven**

---

# API Endpoints Documentation

## Authentication

These endpoints handle registration and session security.

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/auth/signup` | Register a new user |
| `POST` | `/auth/login` | Login and get access and refresh tokens |
| `POST` | `/auth/refresh-token` | Generate new tokens using a refresh token |

## User Management

These endpoints manage the authenticated user's profile.

| Method   | Endpoint                | Description                                 |
|----------|-------------------------|---------------------------------------------|
| `GET`    | `/users/profile`        | Get the authenticated user's profile        |
| `POST`   | `/users/reset-password` | Reset the authenticated user's password     |
| `PUT`    | `/users/update-profile` | Update the authenticated user's profile's info |
| `DELETE` | `/users/delete-profile` | Soft Delete the authenticated user  |

> **Note**
>
> All requests to `/users/**` must include the following header:
>
> ```http
> Authorization: Bearer <token>
> ```

## Project Structure

```text
jwt-template/
├── docker/
│   ├── docker-compose.yml
│   ├── Dockerfile.local
│   ├── init-db/
│   │   └── databases.sql
│   └── nginx/
├── scripts/
│   ├── run_local.sh
│   └── ...
├── src/
│   ├── main/
│   │   ├── java/com/jwttemplate/api/
│   │   │   ├── login/
│   │   │   │   ├── controller/
│   │   │   │   ├── entity/
│   │   │   │   ├── repository/
│   │   │   │   └── service/
│   │   │   ├── security_config/
│   │   │   └── utils/
│   │   └── resources/
│   │       └── application.yml
├── .env.dev
├── .env.prod
├── .gitignore
├── pom.xml
└── README.md
