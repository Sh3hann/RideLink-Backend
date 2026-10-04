# Member 1 — Account & Access Microservice
**Module:** IT3130 - Application Development (Group Assignment)  
**Assigned Student:** Member 1  
**Role:** User Identity, Authentication, JWT & Access Control  
**Rubric Evaluation:** Criterion I1 (15 Marks)  
**Service Port:** `8081`  
**Isolated Database:** `ridelink_account_db` (MongoDB)

---

## 1. Project Overview & Boundaries
This standalone project contains the complete Java 17 and Spring Boot 3.2.x microservice for **Member 1**.
Manages user registration (Rider, Driver, Admin), secure password hashing (BCrypt), JWT token issuance, role-based authorization, and user profile management.

### Independent Database
- Database name: `ridelink_account_db`
- Under the IT3130 rules, this microservice maintains its own independent data store. No shared databases are utilized.

---

## 2. Technology Stack
- **Language:** Java 17 (LTS)
- **Framework:** Spring Boot 3.2.3
- **Data Persistence:** Spring Data MongoDB (`ridelink_account_db`)
- **Documentation:** SpringDoc OpenAPI 2.3.0 (Swagger UI)
- **Containerization:** Docker & Dockerfile included

---

## 3. How to Build & Run This Microservice

### Prerequisites
- JDK 17 installed (`java -version`)
- Apache Maven 3.8+ installed (`mvn -version`)
- MongoDB running locally on port 27017 (e.g. `docker run -d -p 27017:27017 --name account-service-db mongo:6.0`)
- Set `JWT_SECRET` to a random value of at least 32 bytes before starting the service.

### Run Standalone with Maven
```bash
# Generate a signing key for this shell session
export JWT_SECRET="$(openssl rand -base64 32)"

# 1. Clean and compile
mvn clean compile

# 2. Run unit tests
mvn test

# 3. Start the Spring Boot application
mvn spring-boot:run
```
The application will launch and listen on **http://localhost:8081**.

### Access Swagger UI
Open your web browser and navigate to:  
👉 **http://localhost:8081/swagger-ui.html**  
Raw OpenAPI JSON: `http://localhost:8081/v3/api-docs`

### Build & Run via Docker
```bash
docker build -t ridelink/account-service:1.0.0 .
docker run -p 8081:8081 ridelink/account-service:1.0.0
```

---

## 4. Key Endpoints for Member 1
- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `GET /api/v1/users/me`
- `GET /api/v1/users/{id}`
- `GET /api/v1/users`

---

## 5. Demonstration & Submission Note
As specified in the IT3130 assignment brief:
- The completed system can be demonstrated using **Swagger UI** and **Postman**.
- A frontend is not required for marking.
- All code is 100% Java & Spring Boot (MERN stack is not used).
