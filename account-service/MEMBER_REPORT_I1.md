# Individual Assessment Report — Member 1
**Microservice:** Member 1 — Account & Access Microservice (Port 8081)  
**Assigned Student Role:** User Identity, Authentication, JWT & Access Control  
**Database:** ridelink_account_db (MongoDB)  
**Evaluation Criterion:** Criterion I1 (15 Marks)

## 1. Domain Ownership & Responsibilities
Member 1 is solely responsible for:
Manages user registration (Rider, Driver, Admin), secure password hashing (BCrypt), JWT token issuance, role-based authorization, and user profile management.

## 2. Database Isolation
- Stores domain documents in `ridelink_account_db`.
- Other microservices communicate strictly through HTTP REST APIs; zero direct cross-database queries are permitted.

## 3. API Endpoints Implemented
- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `GET /api/v1/users/me`
- `GET /api/v1/users/{id}`
- `GET /api/v1/users`

## 4. Verification Evidence
- Swagger UI available at `http://localhost:8081/swagger-ui.html`.
- All request/response contracts validate with Spring Boot validation annotations.
- Unit and integration tests verify happy paths and negative edge cases.
