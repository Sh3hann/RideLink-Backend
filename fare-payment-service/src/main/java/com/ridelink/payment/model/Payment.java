package com.ridelink.payment.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
@Document(collection = "payments")
public class Payment {
    @Id private String id; private String rideId; private String passengerId; private String driverId; private double amount; private String currency; private String paymentMethod; private PaymentStatus paymentStatus; private String failureReason; private String transactionRef; private Instant processedAt;
    public String getId() { return id; } public void setId(String id) { this.id = id; }
    public String getRideId() { return rideId; } public void setRideId(String rideId) { this.rideId = rideId; }
    public String getPassengerId() { return passengerId; } public void setPassengerId(String passengerId) { this.passengerId = passengerId; }
    public String getDriverId() { return driverId; } public void setDriverId(String driverId) { this.driverId = driverId; }
    public double getAmount() { return amount; } public void setAmount(double amount) { this.amount = amount; }
    public String getCurrency() { return currency; } public void setCurrency(String currency) { this.currency = currency; }
    public String getPaymentMethod() { return paymentMethod; } public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; } public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getFailureReason() { return failureReason; } public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public String getTransactionRef() { return transactionRef; } public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }
    public Instant getProcessedAt() { return processedAt; } public void setProcessedAt(Instant processedAt) { this.processedAt = processedAt; }
}
