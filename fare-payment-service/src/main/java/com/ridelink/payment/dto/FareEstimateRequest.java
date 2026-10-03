package com.ridelink.payment.dto;
public class FareEstimateRequest {
    private String vehicleClass; private double distanceKm; private double estimatedDurationMin;
    public String getVehicleClass() { return vehicleClass; } public void setVehicleClass(String vehicleClass) { this.vehicleClass = vehicleClass; }
    public double getDistanceKm() { return distanceKm; } public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    public double getEstimatedDurationMin() { return estimatedDurationMin; } public void setEstimatedDurationMin(double estimatedDurationMin) { this.estimatedDurationMin = estimatedDurationMin; }
}
