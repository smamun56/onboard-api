# JWT Authentication Template with Spring Boot

This project is a starter template for implementing JWT (JSON Web Token) authentication in a Spring Boot application. It is designed with onboarding in mind, ensuring that new developers can quickly understand and extend the functionality.

## Features

- JWT-based authentication and authorization.
- Role-based access control.
- Spring Security integration.
- Global exception handling.
- Redis integration for session or token caching (optional).
- Easy onboarding with comprehensive documentation.

---

## Prerequisites

To run this project, ensure you have the following installed:

- **Java 17 or later**
- **Maven 3.8+**
- **Spring Boot 3.x**
- **Redis** (optional, depending on your configuration)
- **PostgreSQL** or any preferred database.

---

## Project Structure

### `src/main/java`

- **config**: Contains configuration files, such as `SecurityConfig`, `RedisConfig`, and JWT settings.
- **controller**: REST controllers that handle API requests.
- **dto**: Data Transfer Objects used for communication between client and server.
- **exception**: Custom exceptions and global exception handlers.
- **filter**: Filters like `JwtAuthenticationFilter` to process incoming requests.
- **model**: Entity classes and enums.
- **repository**: Interfaces for database operations.
- **service**: Business logic services.
- **util**: Utility classes such as JWT utilities.

---

## Setup Instructions

1. **Clone the Repository:**

    ```bash
    git clone <repository-url>
    cd jwt-springboot-template
    ```

2. **Configure Database:**

    Update the `application.yml` file with your database credentials:

    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/your_database
        username: your_username
        password: your_password
      jpa:
        hibernate:
          ddl-auto: update
    ```

3. **Run the Application:**

    Use Maven to start the application:

    ```bash
    ./mvnw spring-boot:run
    ```

4. **Test the Application:**

    Use tools like Postman or Swagger to test endpoints.

    Example:
    - Register: `POST /api/auth/register`
    - Login: `POST /api/auth/login`

---

## Onboarding Guide

### Key Components for New Developers

1. **JWT Generation and Validation:**
   - Found in `JwtUtil` class.
   - Tokens are generated during login and validated for each request.

2. **Authentication Flow:**
   - Handled by `JwtAuthenticationFilter` and `SecurityConfig`.

3. **Custom Exceptions:**
   - Defined in the `exception` package.
   - Ensures consistent error responses.

4. **Role Management:**
   - `Role` enum in the `model` package defines user roles.
   - Used in `@PreAuthorize` or directly in service logic.

5. **Redis Integration (Optional):**
   - Configured in `RedisConfig`.
   - Can be used for storing tokens or session data.

---

## Running Onboarding Tests

1. **Check Database Connection:**

    Ensure the database is running and accessible.

2. **Verify JWT Functionality:**

    - Register a new user.
    - Login to retrieve a JWT token.
    - Use the token to access protected resources.

3. **Run Integration Tests:**

    Execute the following command to run tests:

    ```bash
    ./mvnw test
    ```

4. **Swagger Documentation:**

    Access the Swagger UI at `http://localhost:8080/swagger-ui.html` for API documentation.

---

## Deployment

1. **Build the JAR:**

    ```bash
    ./mvnw clean package
    ```

2. **Run the Application:**

    ```bash
    java -jar target/jwt-springboot-template.jar
    ```

---

## Contact

For any questions or feedback, please contact the project maintainer at [smamun56@gmail.com].

