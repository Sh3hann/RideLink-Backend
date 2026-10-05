package com.ridelink.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FareEstimateRequest {
    private String rideId;
    private String vehicleClass;
    private double distanceKm;
    private double estimatedDurationMin;
    private double actualDurationMin;

    public String getRideId() { return rideId; }
    public void setRideId(String rideId) { this.rideId = rideId; }

    public String getVehicleClass() { return vehicleClass; }
    public void setVehicleClass(String vehicleClass) { this.vehicleClass = vehicleClass; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public double getEstimatedDurationMin() {
        if (estimatedDurationMin <= 0 && actualDurationMin > 0) {
            return actualDurationMin;
        }
        return estimatedDurationMin;
    }
    public void setEstimatedDurationMin(double estimatedDurationMin) { this.estimatedDurationMin = estimatedDurationMin; }

    public double getActualDurationMin() { return actualDurationMin; }
    public void setActualDurationMin(double actualDurationMin) { this.actualDurationMin = actualDurationMin; }
}
