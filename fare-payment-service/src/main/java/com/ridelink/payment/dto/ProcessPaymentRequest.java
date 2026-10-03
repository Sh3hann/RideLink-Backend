package com.ridelink.payment.dto;
public class ProcessPaymentRequest {
    private String rideId; private String passengerId; private String driverId; private double amount; private String paymentMethod; private boolean simulateFailure;
    public String getRideId() { return rideId; } public void setRideId(String rideId) { this.rideId = rideId; }
    public String getPassengerId() { return passengerId; } public void setPassengerId(String passengerId) { this.passengerId = passengerId; }
    public String getDriverId() { return driverId; } public void setDriverId(String driverId) { this.driverId = driverId; }
    public double getAmount() { return amount; } public void setAmount(double amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; } public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public boolean isSimulateFailure() { return simulateFailure; } public void setSimulateFailure(boolean simulateFailure) { this.simulateFailure = simulateFailure; }
}
