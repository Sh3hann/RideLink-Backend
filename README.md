<div align="center">

  <img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=10,24,20&height=200&section=header&text=🚗%20RideLink%20Backend&fontSize=46&fontAlignY=36&desc=Scalable%20Spring%20Boot%20Microservices%20%7C%20Distributed%20Cloud%20Architecture&descAlignY=58&descSize=16&fontColor=ffffff" width="100%" alt="RideLink Header" />

  <p align="center">
    <b>A high-throughput, domain-driven microservices backend architecture powering on-demand ride dispatch, real-time driver tracking, automated fare calculation, and secure accounts.</b>
  </p>

  <p align="center">
    <a href="https://github.com/Sh3hann/RideLink-Backend">
      <img src="https://img.shields.io/badge/💻_Source_Code-GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="Source Code" />
    </a>
  </p>

  <p align="center">
    <img src="https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java" />
    <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white" alt="Spring Boot" />
    <img src="https://img.shields.io/badge/MongoDB-Distributed-47A248?style=flat-square&logo=mongodb&logoColor=white" alt="MongoDB" />
    <img src="https://img.shields.io/badge/Swagger-OpenAPI_3.0-85EA2D?style=flat-square&logo=swagger&logoColor=black" alt="Swagger" />
    <img src="https://img.shields.io/badge/Docker-Ready-2496ED?style=flat-square&logo=docker&logoColor=white" alt="Docker" />
    <img src="https://img.shields.io/badge/License-MIT-22c55e?style=flat-square" alt="License" />
  </p>

</div>

---

## 🏗️ Microservices Architecture

Each domain is strictly decoupled with dedicated database persistence boundaries, enabling zero-downtime rolling deployments and horizontal scaling:

`mermaid
graph TD
    subgraph Gateway ["API Ingress & Clients"]
        CLIENT["Mobile / Web Clients"]
        GATEWAY["API Gateway & Reverse Proxy"]
    end

    subgraph CoreServices ["Distributed Spring Boot Microservices"]
        ACCOUNT["Account Service<br/>:8081"]
        DRIVER["Driver & Vehicle Service<br/>:8082"]
        RIDE["Ride Management Service<br/>:8083"]
        PAYMENT["Fare & Payment Service<br/>:8084"]
    end

    subgraph DataTier ["Isolated Database Boundaries"]
        DB_ACC[("ridelink_account_db")]
        DB_DRV[("driver_db")]
        DB_RIDE[("ride_db")]
        DB_PAY[("fare_payment_db")]
    end

    CLIENT --> GATEWAY
    GATEWAY --> ACCOUNT
    GATEWAY --> DRIVER
    GATEWAY --> RIDE
    GATEWAY --> PAYMENT

    ACCOUNT --> DB_ACC
    DRIVER --> DB_DRV
    RIDE --> DB_RIDE
    PAYMENT --> DB_PAY
`

---

## ⚡ Microservice Domain Matrix

<div align="center">

| Microservice | Port | Database Scope | Key Responsibilities |
|:---|:---|:---|:---|
| **Account Service** | 8081 | idelink_account_db | User authentication, identity tokens, and profile management. |
| **Driver & Vehicle Service** | 8082 | driver_db | Driver onboarding, vehicle registration, status queues, and area dispatch. |
| **Ride Management Service** | 8083 | ide_db | Trip creation, route tracking, state transitions (REQUESTED → COMPLETED). |
| **Fare & Payment Service** | 8084 | are_payment_db | Dynamic surge pricing, fare estimation, invoices, and payment processing. |

</div>

<details>
<summary><b>📂 View Microservice Repository Structure</b></summary>

`
RideLink-Backend/
├── account-service/           # User lifecycle & JWT credentials
├── driver-service/            # Driver profiles, vehicle classes & dispatch state
├── ride-service/              # Ride lifecycle & active trip coordinator
├── fare-payment-service/      # Pricing calculator & transactional ledger
└── .github/                   # Automated CI workflow pipelines
`

</details>

---

## 🚀 Getting Started

### Prerequisites
- **Java 21 (JDK)**
- **Maven 3.8+**
- **MongoDB** (Local or Docker daemon)

### 1. Build and Run a Specific Service
To run any independent service (e.g., Driver Service):

`ash
cd driver-service
mvn clean install
mvn spring-boot:run
`

### 2. Run Isolated Automated Unit Tests
`ash
mvn clean test
`

### 3. Interactive API Documentation (Swagger)
When a service is running, explore and test the endpoints via interactive OpenAPI Swagger docs:
`
http://localhost:<PORT>/swagger-ui.html
`

---

<div align="center">

### 👨‍💻 Developed with Precision by [Akil Shehan](https://github.com/Sh3hann)

[![Website](https://img.shields.io/badge/Portfolio-itzshehan.xyz-f59e0b?style=flat-square&logo=google-chrome&logoColor=white)](https://itzshehan.xyz/)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Akil_Shehan-0077b5?style=flat-square&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/akil-shehan-b808b340b)
[![GitHub](https://img.shields.io/badge/GitHub-@Sh3hann-181717?style=flat-square&logo=github&logoColor=white)](https://github.com/Sh3hann)

<sub>© 2026 RideLink Backend. Released under the MIT License.</sub>

</div>