package com.ridelink.driver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "driver_profiles")
public class DriverProfile {
    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    private String licenseNumber;
    private int experienceYears;
    private double rating = 5.0;
    private Vehicle vehicle;
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.OFFLINE;
    private String serviceArea;
    private GeoLocation currentLocation;
    private String activeRideId;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public DriverProfile() {}
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public AvailabilityStatus getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    public String getServiceArea() { return serviceArea; }
    public void setServiceArea(String serviceArea) { this.serviceArea = serviceArea; }
    public GeoLocation getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(GeoLocation currentLocation) { this.currentLocation = currentLocation; }
    public String getActiveRideId() { return activeRideId; }
    public void setActiveRideId(String activeRideId) { this.activeRideId = activeRideId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public boolean isAvailable() {
        return availabilityStatus == AvailabilityStatus.AVAILABLE;
    }

    public String getVehicleClass() {
        return vehicle != null && vehicle.getVehicleClass() != null ? vehicle.getVehicleClass().name() : null;
    }

    public String getVehicleLicensePlate() {
        return vehicle != null ? vehicle.getLicensePlate() : null;
    }
}