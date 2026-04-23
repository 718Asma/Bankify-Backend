# Bankify Backend



Bankify is a school group project from **ENICAR** for a modern banking website.
This repository contains the **Spring Boot** backend that powers the **Angular** frontend.



## ✨ Overview

Bankify backend manages the main banking flows for the application:

- 👤 client and agent accounts
- 🏦 bank accounts and balances
- 💸 transactions
- 🔔 notifications
- 🔐 authentication and security support

## 🛠 Tech Stack

- ☕ Java 17
- 🌱 Spring Boot
- 🗃 Spring Data JPA
- 🔒 Spring Security
- 🐬 MySQL
- 🧩 Lombok
- ⚡ Angular frontend integration
  <p align="center">
	<img src="https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java 17 badge" />
	<img src="https://img.shields.io/badge/Maven-Build-C71A36?style=flat-square&logo=apachemaven&logoColor=white" alt="Maven badge" />
	<img src="https://img.shields.io/badge/MySQL-Database-4479A1?style=flat-square&logo=mysql&logoColor=white" alt="MySQL badge" />
	<img src="https://img.shields.io/badge/Spring_Data_JPA-Repository-6DB33F?style=flat-square&logo=spring&logoColor=white" alt="Spring Data JPA badge" />
	<img src="https://img.shields.io/badge/Spring_Security-Secured-6DB33F?style=flat-square&logo=springsecurity&logoColor=white" alt="Spring Security badge" />
	<img src="https://img.shields.io/badge/Lombok-Annotations-BC2D52?style=flat-square&logo=lombok&logoColor=white" alt="Lombok badge" />
</p>



## 🧱 Project Structure

```text
src/main/java/com/ebanking/backend/
├── controller/        # REST endpoints
├── model/             # JPA entities
├── repository/        # Spring Data repositories
├── service/           # Service contracts
└── serviceImplement/  # Service implementations
```

## 🚀 Getting Started

### ✅ Prerequisites

- JDK 17
- Maven
- MySQL
- Node.js and Angular CLI for the frontend

### ⚙️ Backend Setup

1. Create a MySQL database named `bankify_db`.
2. Update `src/main/resources/application.properties` with your local database credentials.
3. Make sure the frontend is configured to call the backend API on `http://localhost:8080`.

### ▶️ Run the Backend

On Linux use:

```bash
./mvnw spring-boot:run
```

On Windows use:

```powershell
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

## 🔧 Configuration

The backend uses `application.properties` for local development settings.

Important values to review:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `token.secret`
- `token.expiration`

For a real deployment, keep secrets out of the repository and move them to environment variables or an external secrets store.

## 📝 Notes

- The frontend for this project is implemented with Angular.
- The backend follows a layered architecture with controllers, services, repositories, and JPA models.
- Entities are mapped to the banking domain described in the project diagram.

## 🎓 Team Project

This project is part of a school group assignment at ENICAR and is intended to demonstrate a full banking website architecture with a Java backend and Angular frontend.
