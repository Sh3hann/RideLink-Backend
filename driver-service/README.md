# RideLink - Backend Microservices

This repository contains the backend microservices solution for the RideLink Ride-Sharing Platform, developed for the IT3130 Application Development Group Assignment.

IT24102480 - Basiron A.S
Driver-Service

## Microservice Ownership

| Microservice | Port | Database | Owner |
|---|---|---|---|
| **Account Service** | 8081 | `account_db` | Member 1 |
| **Driver & Vehicle Service** | 8082 | `driver_db` | Member 2 |
| **Ride Management Service** | 8083 | `ride_db` | Member 3 |
| **Fare & Payment Service** | 8084 | `fare_payment_db` | Member 4 |

## Prerequisites
- **Java 17** and **Maven** installed.
- **Docker** (Docker Desktop) installed and running.
- **Postman** (for testing the shared collection).

## Configuration & Database Setup
Each microservice is entirely independent and maintains its own data persistence boundary. They are configured via their respective `application.properties` to connect to isolated MongoDB databases.

To spin up the shared MongoDB database server, run this single Docker command in your terminal:
```bash
docker run --name ridelink-mongo -d -p 27017:27017 mongo:latest
```
*(If the container is already created, you can simply run `docker start ridelink-mongo`)*

## Start-up Order
Because the services communicate with each other (e.g. Ride Service calls Driver Service and Fare Service via OpenFeign), the recommended startup order is:
1. Start the Docker MongoDB container.
2. Start the **Account Service**.
3. Start the **Driver & Vehicle Service**.
4. Start the **Fare & Payment Service**.
5. Start the **Ride Management Service**.

## Commands

**To run a microservice:**
Navigate into the specific microservice folder (e.g., `cd driver-service`) and run:
```bash
mvn spring-boot:run
```

**To run the unit tests:**
Navigate into the specific microservice folder and run:
```bash
mvn clean test
```

## Endpoint Locations (Swagger UI)
Since no frontend is required, each service exposes a Swagger UI for API exploration and testing. Once the services are running, they can be accessed at:
- **Account Service:** http://localhost:8081/swagger-ui/index.html
- **Driver Service:** http://localhost:8082/swagger-ui/index.html
- **Ride Service:** http://localhost:8083/swagger-ui/index.html
- **Fare & Payment Service:** http://localhost:8084/swagger-ui/index.html

## Sample Test Data
Here is a sample payload you can use in the Driver Service Swagger UI (`POST /api/drivers`) to quickly create a test driver profile:

```json
{
  "userId": "user-123",
  "licenseNumber": "B-9876543",
  "experienceYears": 5,
  "serviceArea": "Colombo",
  "vehicle": {
    "make": "Toyota",
    "model": "Prius",
    "licensePlate": "WP-CBA-1234",
    "color": "White",
    "vehicleClass": "STANDARD"
  }
}