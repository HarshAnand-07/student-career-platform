
# Student Career Platform

A backend application built with Java and Spring Boot to help students manage their career profiles and explore career opportunities.

## Tech Stack

- **Language:** Java
- **Backend:** Spring Boot
- **Database:** MySQL
- **Security:** Spring Security and JWT Authentication
- **API Documentation:** Swagger UI / OpenAPI
- **Build Tool:** Maven

## Features

- Student registration and login
- JWT-based authentication and authorization
- Student profile management
- Skills, projects, and certifications management
- Career opportunity management
- Opportunity search, filtering, and pagination
- Application management and application status tracking
- Role-based access control
- Input validation and centralized exception handling
- Interactive REST API documentation using Swagger UI

## Getting Started

### Prerequisites

- Java 21
- MySQL Server
- Git

### Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/HarshAnand-07/student-career-platform.git
   ```

2. Open the project in IntelliJ IDEA or another Java IDE.

3. Create a MySQL database named `Student_Carrer`.

4. Configure the following environment variables in your IDE:

    - `DB_USERNAME` — your MySQL username
    - `DB_PASSWORD` — your MySQL password
    - `JWT_SECRET` — your JWT signing secret

5. Run the application using the IDE or Maven:

   ```bash
   ./mvnw spring-boot:run
   ```

   On Windows:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

## API Documentation

After starting the application, open Swagger UI:

`http://localhost:8080/swagger-ui/index.html`



## Security

- JWT-based authentication
- Role-based authorization for restricted endpoints
- Student profile access checks

## Testing

Run the test suite using Maven Wrapper:

**Windows:**
```powershell
.\mvnw.cmd test
```

**macOS/Linux:**
```bash
./mvnw test
```

Configure the required environment variables (`DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET`) before running tests.


## Configuration

Database credentials and the JWT signing secret are provided through environment variables. Do not commit real credentials or secrets to the repository.

## Project Status

Under development.
