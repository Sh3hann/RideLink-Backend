# Build stage with JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Runtime stage with JRE 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=build /app/target/ride-service-1.0.0.jar app.jar

EXPOSE 8083

ENV PORT=8083 \
    MONGODB_URI=mongodb://mongo:27017/ridelink_ride_db \
    DRIVER_SERVICE_URL=http://driver-service:8082 \
    FARE_SERVICE_URL=http://fare-service:8084 \
    ACCOUNT_SERVICE_URL=http://account-service:8081

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8083/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
