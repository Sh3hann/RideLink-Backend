package com.ridelink.payment.dto;
import com.ridelink.payment.model.FareBreakdown;
public class FareEstimateResponse {
    private double distanceKm; private FareBreakdown breakdown; private double totalEstimatedFare; private String currency;
    public double getDistanceKm() { return distanceKm; } public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    public FareBreakdown getBreakdown() { return breakdown; } public void setBreakdown(FareBreakdown breakdown) { this.breakdown = breakdown; }
    public double getTotalEstimatedFare() { return totalEstimatedFare; } public void setTotalEstimatedFare(double totalEstimatedFare) { this.totalEstimatedFare = totalEstimatedFare; }
    public String getCurrency() { return currency; } public void setCurrency(String currency) { this.currency = currency; }
}
