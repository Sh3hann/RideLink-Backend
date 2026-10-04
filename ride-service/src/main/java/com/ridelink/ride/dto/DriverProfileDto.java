package com.ridelink.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Driver profile representation received from Driver & Vehicle Service (Microservice 2)")
public class DriverProfileDto {

    @Schema(description = "Driver identifier", example = "drv_404")
    private String id;

    @Schema(description = "Driver full name", example = "Kasun Perera")
    private String fullName;

    @Schema(description = "Driver phone number", example = "+94771234567")
    private String phoneNumber;

    @Schema(description = "Operational service area", example = "COLOMBO_CENTRAL")
    private String serviceArea;

    @Schema(description = "Vehicle class (CAR_SEDAN, TUK, VAN, BIKE)", example = "CAR_SEDAN")
    private String vehicleClass;

    @Schema(description = "Vehicle registration plate", example = "WP CAB-4521")
    private String vehicleLicensePlate;

    @Schema(description = "Driver availability flag", example = "true")
    private boolean available;

    @Schema(description = "Driver rating (out of 5.0)", example = "4.9")
    private double rating;

    @Schema(description = "ID of active ride if currently assigned", example = "null")
    private String activeRideId;

    public DriverProfileDto() {}

    public DriverProfileDto(String id, String fullName, String serviceArea, String vehicleClass) {
        this.id = id;
        this.fullName = fullName;
        this.serviceArea = serviceArea;
        this.vehicleClass = vehicleClass;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(String vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    public void setVehicleLicensePlate(String vehicleLicensePlate) {
        this.vehicleLicensePlate = vehicleLicensePlate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getActiveRideId() {
        return activeRideId;
    }

    public void setActiveRideId(String activeRideId) {
        this.activeRideId = activeRideId;
    }
}
