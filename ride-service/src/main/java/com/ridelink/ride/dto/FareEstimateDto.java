package com.ridelink.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Fare estimation payload received from Fare & Payment Service (Microservice 4)")
public class FareEstimateDto {

    @Schema(description = "Base fare amount", example = "150.00")
    private double baseFare;

    @Schema(description = "Distance component of fare", example = "400.00")
    private double distanceFare;

    @Schema(description = "Duration component of fare", example = "100.00")
    private double timeFare;

    @Schema(description = "Surge pricing multiplier (1.0 = normal)", example = "1.0")
    private double surgeMultiplier;

    @Schema(description = "Calculated total estimated fare", example = "650.00")
    private double totalEstimatedFare;

    @Schema(description = "Currency code", example = "LKR")
    private String currency;

    public FareEstimateDto() {
        this.currency = "LKR";
        this.surgeMultiplier = 1.0;
    }

    public FareEstimateDto(double totalEstimatedFare) {
        this();
        this.totalEstimatedFare = totalEstimatedFare;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(double baseFare) {
        this.baseFare = baseFare;
    }

    public double getDistanceFare() {
        return distanceFare;
    }

    public void setDistanceFare(double distanceFare) {
        this.distanceFare = distanceFare;
    }

    public double getTimeFare() {
        return timeFare;
    }

    public void setTimeFare(double timeFare) {
        this.timeFare = timeFare;
    }

    public double getSurgeMultiplier() {
        return surgeMultiplier;
    }

    public void setSurgeMultiplier(double surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
    }

    public double getTotalEstimatedFare() {
        return totalEstimatedFare;
    }

    public void setTotalEstimatedFare(double totalEstimatedFare) {
        this.totalEstimatedFare = totalEstimatedFare;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
