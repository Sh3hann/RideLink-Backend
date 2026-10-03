# Individual Assessment Report — Member 4
**Microservice:** Member 4 — Fare & Payment Microservice (Port 8084)  
**Assigned Student Role:** Fare Estimation Formula, Payment Processing & Receipts  
**Database:** ridelink_fare_payment_db (MongoDB)  
**Evaluation Criterion:** Criterion I4 (15 Marks)

## 1. Domain Ownership & Responsibilities
Member 4 is solely responsible for:
Implements dynamic fare calculation formulas (base + distance + duration * vehicle multiplier * surge), multi-channel payment processing (CASH, CARD, WALLET), simulated gateway failure testing, and tamper-proof digital receipts.

## 2. Database Isolation
- Stores domain documents in `ridelink_fare_payment_db`.
- Other microservices communicate strictly through HTTP REST APIs; zero direct cross-database queries are permitted.

## 3. API Endpoints Implemented
- `POST /api/v1/fares/estimate`
- `POST /api/v1/payments/process`
- `GET /api/v1/payments/{id}`
- `GET /api/v1/payments/ride/{rideId}`
- `GET /api/v1/receipts/{receiptNumber}`

## 4. Verification Evidence
- Swagger UI available at `http://localhost:8084/swagger-ui.html`.
- All request/response contracts validate with Spring Boot validation annotations.
- Unit and integration tests verify happy paths and negative edge cases.
