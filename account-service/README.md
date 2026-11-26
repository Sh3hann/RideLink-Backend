# Account & Access Microservice

**Service Port:** `8081`  
**Isolated Database:** `ridelink_account_db` (MongoDB)

---

## 1. Project Overview & Boundaries

Standalone Java 21 and Spring Boot 3.2.3 microservice handling user registration (`Rider`, `Driver`, `Admin`), BCrypt password hashing, JWT token issuance, role-based access control, and user profile management.

### Independent Database
- **Database name:** `ridelink_account_db`
- Maintains its own independent MongoDB boundary with zero direct cross-database queries.

---

## 2. Technology Stack
- **Language:** Java 21 (LTS)
- **Framework:** Spring Boot 3.2.3
- **Data Persistence:** Spring Data MongoDB (`ridelink_account_db`)
- **Security:** Spring Security + JJWT (`0.11.5`)
- **Documentation:** SpringDoc OpenAPI 2.3.0 (Swagger UI)

---

## 3. Build & Run

### Prerequisites
- **JDK 21** installed (`java -version`)
- **Apache Maven 3.8+** installed (`mvn -version`)
- **MongoDB** running locally on port `27017` (e.g., `docker run -d -p 27017:27017 --name account-service-db mongo:6.0`)
- **`JWT_SECRET`** environment variable configured (at least 32 random bytes)

### Run Standalone with Maven
```bash
# Generate a signing key for the current shell session
export JWT_SECRET="$(openssl rand -base64 32)"

# Optional: override default MongoDB URI
export SPRING_DATA_MONGODB_URI="mongodb://localhost:27017/ridelink_account_db"

# Compile, test, and run
mvn clean compile
mvn test
mvn spring-boot:run
```

The service listens on **http://localhost:8081**.

### OpenAPI & Swagger UI
- **Swagger UI:** `http://localhost:8081/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8081/v3/api-docs` (or inspect `openapi-account.json`)

---

## 4. Key Endpoints
- `POST /api/v1/auth/register` — Register a new user
- `POST /api/v1/auth/login` — Authenticate and issue JWT
- `GET /api/v1/users/me` — Fetch authenticated profile
- `GET /api/v1/users/{id}` — Retrieve user by ID
- `GET /api/v1/users` — List users
