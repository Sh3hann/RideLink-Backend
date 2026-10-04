package com.ridelink.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request payload for creating a new ride request")
public class CreateRideRequest {

    @Schema(description = "Passenger ID requesting the ride", example = "usr_pass_101", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "passengerId is required")
    private String passengerId;

    @Schema(description = "Pickup location or address", example = "Colombo Fort Railway Station", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "pickupLocation is required")
    private String pickupLocation;

    @Schema(description = "Destination address", example = "Galle Face Green, Colombo 03", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "destinationLocation is required")
    private String destinationLocation;

    @Schema(description = "Service area code", example = "COLOMBO_CENTRAL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "serviceArea is required")
    private String serviceArea;

    @Schema(description = "Vehicle category (CAR_SEDAN, TUK, VAN, BIKE)", example = "CAR_SEDAN", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "vehicleClass is required")
    private String vehicleClass;

    @Schema(description = "Distance in kilometers", example = "5.8", requiredMode = Schema.RequiredMode.REQUIRED)
    @Positive(message = "distanceKm must be greater than 0")
    private double distanceKm;

    @Schema(description = "Estimated duration in minutes", example = "16.5", requiredMode = Schema.RequiredMode.REQUIRED)
    @Positive(message = "estimatedDurationMin must be greater than 0")
    private double estimatedDurationMin;

    @Schema(description = "Optional rider notes", example = "Waiting at main entrance")
    private String notes;

    public CreateRideRequest() {}

    public CreateRideRequest(String passengerId, String pickupLocation, String destinationLocation,
                             String serviceArea, String vehicleClass, double distanceKm, double estimatedDurationMin) {
        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.destinationLocation = destinationLocation;
        this.serviceArea = serviceArea;
        this.vehicleClass = vehicleClass;
        this.distanceKm = distanceKm;
        this.estimatedDurationMin = estimatedDurationMin;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
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

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double getEstimatedDurationMin() {
        return estimatedDurationMin;
    }

    public void setEstimatedDurationMin(double estimatedDurationMin) {
        this.estimatedDurationMin = estimatedDurationMin;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
