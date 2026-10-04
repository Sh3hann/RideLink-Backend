package com.ridelink.ride.dto;

import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Standardized response payload representing ride details")
public class RideResponse {

    @Schema(description = "Ride ID", example = "66f4a8b2c91d8e123456789a")
    private String id;

    @Schema(description = "Passenger identifier", example = "usr_pass_101")
    private String passengerId;

    @Schema(description = "Assigned driver identifier", example = "drv_404")
    private String driverId;

    @Schema(description = "Pickup location", example = "Colombo Fort Railway Station")
    private String pickupLocation;

    @Schema(description = "Destination location", example = "Galle Face Green, Colombo 03")
    private String destinationLocation;

    @Schema(description = "Operating service area", example = "COLOMBO_CENTRAL")
    private String serviceArea;

    @Schema(description = "Vehicle category requested", example = "CAR_SEDAN")
    private String vehicleClass;

    @Schema(description = "Trip distance in km", example = "5.8")
    private double distanceKm;

    @Schema(description = "Estimated duration in minutes", example = "16.5")
    private double estimatedDurationMin;

    @Schema(description = "Estimated upfront fare (LKR)", example = "650.00")
    private double estimatedFare;

    @Schema(description = "Final charged fare (LKR)", example = "650.00")
    private double finalFare;

    @Schema(description = "Current lifecycle status")
    private RideStatus status;

    @Schema(description = "Cancellation reason if cancelled")
    private String cancellationReason;

    @Schema(description = "Actor who cancelled the ride")
    private String cancelledBy;

    @Schema(description = "Ride notes")
    private String notes;

    @Schema(description = "Timestamp when ride was requested")
    private Instant requestedAt;

    @Schema(description = "Timestamp when driver was assigned")
    private Instant assignedAt;

    @Schema(description = "Timestamp when driver accepted")
    private Instant acceptedAt;

    @Schema(description = "Timestamp when trip started")
    private Instant startedAt;

    @Schema(description = "Timestamp when trip was completed")
    private Instant completedAt;

    @Schema(description = "Timestamp when trip was cancelled")
    private Instant cancelledAt;

    public RideResponse() {}

    public static RideResponse fromEntity(Ride ride) {
        if (ride == null) return null;
        RideResponse resp = new RideResponse();
        resp.id = ride.getId();
        resp.passengerId = ride.getPassengerId();
        resp.driverId = ride.getDriverId();
        resp.pickupLocation = ride.getPickupLocation();
        resp.destinationLocation = ride.getDestinationLocation();
        resp.serviceArea = ride.getServiceArea();
        resp.vehicleClass = ride.getVehicleClass();
        resp.distanceKm = ride.getDistanceKm();
        resp.estimatedDurationMin = ride.getEstimatedDurationMin();
        resp.estimatedFare = ride.getEstimatedFare();
        resp.finalFare = ride.getFinalFare();
        resp.status = ride.getStatus();
        resp.cancellationReason = ride.getCancellationReason();
        resp.cancelledBy = ride.getCancelledBy();
        resp.notes = ride.getNotes();
        resp.requestedAt = ride.getRequestedAt();
        resp.assignedAt = ride.getAssignedAt();
        resp.acceptedAt = ride.getAcceptedAt();
        resp.startedAt = ride.getStartedAt();
        resp.completedAt = ride.getCompletedAt();
        resp.cancelledAt = ride.getCancelledAt();
        return resp;
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
}
