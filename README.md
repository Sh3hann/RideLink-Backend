# RideLink Backend Microservices

This is the central repository for the RideLink Ride-Sharing Platform backend, developed for the IT3130 Application Development Group Assignment.

## Group Organization & Microservice Ownership

| Microservice | Port | Database | Primary Owner | Status |
|---|---|---|---|---|
| **Account Service** | 8081 | `account_db` | Member 1 | Pending |
| **Driver & Vehicle Service** | 8082 | `driver_db` | Member 2 (IT24102480 - Basiron A.S) | Completed |
| **Ride Management Service** | 8083 | `ride_db` | Member 3 (IT24102654 - Balapatabandige D.H.) | Completed |
| **Fare & Payment Service** | 8084 | `fare_payment_db` | Member 4 (IT24102470 - Jayawardhana T.N.D.J) | Completed |

## Prerequisites
- **Java 21**
- **Maven 3.8+**
- **MongoDB** (Local or via Docker)
- **Postman** (for testing the shared collection)

## Configuration & Database Setup
Each microservice is entirely independent and maintains its own data persistence boundary. They are configured via their respective `application.properties` to connect to isolated MongoDB databases.

You can set the `MONGO_URI` environment variable to override the default local database. For example:
`export MONGO_URI=mongodb://localhost:27017/driver_db`

## Start-Up Order
As the services are loosely coupled, they can be started independently. However, for full integration testing, start them in this order:
1. Account Service (for user tokens)
2. Driver & Vehicle Service
3. Ride Management Service
4. Fare & Payment Service

## Build and Run Commands
To build and run any individual service, navigate to its folder and run:

```bash
cd driver-service
mvn clean install
mvn spring-boot:run
```

## Test Instructions
Each service contains isolated unit tests. To run the tests for a specific service:

```bash
cd driver-service
mvn clean test
```

## API Endpoints & Testing
- **Swagger UI:** Accessible at `http://localhost:<port>/swagger-ui.html` when a service is running.
- **Postman Collection:** A shared Postman collection containing happy and negative paths is located in the `/postman` directory of each implemented service.

### Sample Data (Driver Service)
**POST /api/drivers**
```json
{
  "userId": "usr_123",
  "licenseNumber": "LIC-9876",
  "experienceYears": 5,
  "serviceArea": "Downtown",
  "vehicle": {
    "make": "Toyota",
    "model": "Prius",
    "licensePlate": "WP-CBA-1234",
    "vehicleClass": "STANDARD"
  }
}
```
