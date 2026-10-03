package com.ridelink.payment.model;
public class FareBreakdown {
    private double baseFare; private double distanceCost; private double timeCost; private double surgeMultiplier; private double subtotal; private double tax; private double total;
    public FareBreakdown() {}
    public FareBreakdown(double baseFare, double distanceCost, double timeCost, double surgeMultiplier, double subtotal, double tax, double total) {
        this.baseFare = baseFare; this.distanceCost = distanceCost; this.timeCost = timeCost; this.surgeMultiplier = surgeMultiplier; this.subtotal = subtotal; this.tax = tax; this.total = total;
    }
    public double getBaseFare() { return baseFare; }
    public void setBaseFare(double baseFare) { this.baseFare = baseFare; }
    public double getDistanceCost() { return distanceCost; }
    public void setDistanceCost(double distanceCost) { this.distanceCost = distanceCost; }
    public double getTimeCost() { return timeCost; }
    public void setTimeCost(double timeCost) { this.timeCost = timeCost; }
    public double getSurgeMultiplier() { return surgeMultiplier; }
    public void setSurgeMultiplier(double surgeMultiplier) { this.surgeMultiplier = surgeMultiplier; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
