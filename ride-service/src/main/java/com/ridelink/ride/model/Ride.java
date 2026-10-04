package com.ridelink.ride.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "rides")
@Schema(description = "Ride entity representing ride lifecycle and orchestration data")
public class Ride {

    @Id
    @Schema(description = "Unique ride identifier (MongoDB ObjectId)", example = "66f4a8b2c91d8e123456789a")
    private String id;

    @Indexed
    @Schema(description = "Identifier of passenger requesting the ride", example = "usr_pass_101")
    private String passengerId;

    @Indexed
    @Schema(description = "Identifier of assigned driver", example = "drv_404")
    private String driverId;

    @Schema(description = "Pickup location or address", example = "Colombo Fort Railway Station")
    private String pickupLocation;

    @Schema(description = "Destination location or address", example = "Galle Face Green")
    private String destinationLocation;

    @Indexed
    @Schema(description = "Operating service area", example = "COLOMBO_CENTRAL")
    private String serviceArea;

    @Schema(description = "Vehicle category requested (e.g., CAR_SEDAN, TUK, VAN, BIKE)", example = "CAR_SEDAN")
    private String vehicleClass;

    @Schema(description = "Calculated route distance in kilometers", example = "5.8")
    private double distanceKm;

    @Schema(description = "Estimated trip duration in minutes", example = "16.5")
    private double estimatedDurationMin;

    @Schema(description = "Estimated upfront fare in local currency (LKR)", example = "650.00")
    private double estimatedFare;

    @Schema(description = "Final calculated fare at completion", example = "650.00")
    private double finalFare;

    @Indexed
    @Schema(description = "Current lifecycle status of the ride")
    private RideStatus status;

    @Schema(description = "Reason for cancellation if cancelled", example = "Passenger requested cancellation")
    private String cancellationReason;

    @Schema(description = "Actor who initiated cancellation (PASSENGER, DRIVER, SYSTEM)", example = "PASSENGER")
    private String cancelledBy;

    @Schema(description = "Optional ride notes or special instructions", example = "Near front gate")
    private String notes;

    @Schema(description = "Timestamp when ride was requested")
    private Instant requestedAt;

    @Schema(description = "Timestamp when driver was assigned")
    private Instant assignedAt;

    @Schema(description = "Timestamp when driver accepted the ride")
    private Instant acceptedAt;

    @Schema(description = "Timestamp when ride started (trip in progress)")
    private Instant startedAt;

    @Schema(description = "Timestamp when ride was completed")
    private Instant completedAt;

    @Schema(description = "Timestamp when ride was cancelled")
    private Instant cancelledAt;

    @Schema(description = "Timestamp when ride record was last updated")
    private Instant updatedAt;

    public Ride() {
        this.status = RideStatus.REQUESTED;
        this.requestedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Ride(String passengerId, String pickupLocation, String destinationLocation,
                String serviceArea, String vehicleClass, double distanceKm, double estimatedDurationMin) {
        this();
        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.destinationLocation = destinationLocation;
        this.serviceArea = serviceArea;
        this.vehicleClass = vehicleClass;
        this.distanceKm = distanceKm;
        this.estimatedDurationMin = estimatedDurationMin;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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

    public double getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    public double getFinalFare() {
        return finalFare;
    }

    public void setFinalFare(double finalFare) {
        this.finalFare = finalFare;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Instant getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(Instant acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
