# Individual Assessment Report — Member 2
**Microservice:** Member 2 — Driver & Vehicle Microservice (Port 8082)  
**Assigned Student Role:** Driver Profiles, Vehicle Fleet & Real-Time Availability  
**Database:** ridelink_driver_db (MongoDB)  
**Evaluation Criterion:** Criterion I2 (15 Marks)

## 1. Domain Ownership & Responsibilities
Member 2 is solely responsible for:
Maintains driver operational profiles, vehicle taxonomy (make, model, license plate, vehicle class), real-time availability toggling, geolocation proximity queries, and active trip verification.

## 2. Database Isolation
- Stores domain documents in `ridelink_driver_db`.
- Other microservices communicate strictly through HTTP REST APIs; zero direct cross-database queries are permitted.

## 3. API Endpoints Implemented
- `POST /api/v1/drivers`
- `GET /api/v1/drivers/{id}`
- `PUT /api/v1/drivers/{id}/availability`
- `GET /api/v1/drivers/eligible`
- `GET /api/v1/drivers/{id}/active-ride`

## 4. Verification Evidence
- Swagger UI available at `http://localhost:8082/swagger-ui.html`.
- All request/response contracts validate with Spring Boot validation annotations.
- Unit and integration tests verify happy paths and negative edge cases.
