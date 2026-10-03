# Member 4 — Fare & Payment Microservice
**Module:** IT3130 - Application Development (Group Assignment)  
**Assigned Student:** Member 4  
**Role:** Fare Estimation Formula, Payment Processing & Receipts  
**Rubric Evaluation:** Criterion I4 (15 Marks)  
**Service Port:** `8084`  
**Isolated Database:** `ridelink_fare_payment_db` (MongoDB)

---

## 1. Project Overview & Boundaries
This standalone project contains the complete Java 17 and Spring Boot 3.2.x microservice for **Member 4**.
Implements dynamic fare calculation formulas (base + distance + duration * vehicle multiplier * surge), multi-channel payment processing (CASH, CARD, WALLET), simulated gateway failure testing, and tamper-proof digital receipts.

### Independent Database
- Database name: `ridelink_fare_payment_db`
- Under the IT3130 rules, this microservice maintains its own independent data store. No shared databases are utilized.

---

## 2. Technology Stack
- **Language:** Java 17 (LTS)
- **Framework:** Spring Boot 3.2.3
- **Data Persistence:** Spring Data MongoDB (`ridelink_fare_payment_db`)
- **Documentation:** SpringDoc OpenAPI 2.3.0 (Swagger UI)
- **Containerization:** Docker & Dockerfile included

---

## 3. How to Build & Run This Microservice

### Prerequisites
- JDK 17 installed (`java -version`)
- Apache Maven 3.8+ installed (`mvn -version`)
- MongoDB running locally on port 27017 (e.g. `docker run -d -p 27017:27017 --name fare-payment-service-db mongo:6.0`)

### Run Standalone with Maven
```bash
# 1. Clean and compile
mvn clean compile

# 2. Run unit tests
mvn test

# 3. Start the Spring Boot application
mvn spring-boot:run
```
The application will launch and listen on **http://localhost:8084**.

### Access Swagger UI
Open your web browser and navigate to:  
👉 **http://localhost:8084/swagger-ui.html**  
Raw OpenAPI JSON: `http://localhost:8084/v3/api-docs`

### Build & Run via Docker
```bash
docker build -t ridelink/fare-payment-service:1.0.0 .
docker run -p 8084:8084 ridelink/fare-payment-service:1.0.0
```

---

## 4. Key Endpoints for Member 4
- `POST /api/v1/fares/estimate`
- `POST /api/v1/payments/process`
- `GET /api/v1/payments/{id}`
- `GET /api/v1/payments/ride/{rideId}`
- `GET /api/v1/receipts/{receiptNumber}`

---

## 5. Demonstration & Submission Note
As specified in the IT3130 assignment brief:
- The completed system can be demonstrated using **Swagger UI** and **Postman**.
- A frontend is not required for marking.
- All code is 100% Java & Spring Boot (MERN stack is not used).
