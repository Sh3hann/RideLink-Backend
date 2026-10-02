package com.ridelink.driver.dto;

import com.ridelink.driver.model.Vehicle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class DriverProfileRequest {
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @PositiveOrZero(message = "Experience years must be zero or positive")
    private int experienceYears;

    @NotBlank(message = "Service area is required")
    private String serviceArea;

    @NotNull(message = "Vehicle details are required")
    private Vehicle vehicle;

    public DriverProfileRequest() {}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public String getServiceArea() { return serviceArea; }
    public void setServiceArea(String serviceArea) { this.serviceArea = serviceArea; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
}
