package com.ridelink.payment.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
@Document(collection = "receipts")
public class Receipt {
    @Id private String id; private String receiptNumber; private String paymentId; private String rideId; private Instant issuedAt;
    public String getId() { return id; } public void setId(String id) { this.id = id; }
    public String getReceiptNumber() { return receiptNumber; } public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }
    public String getPaymentId() { return paymentId; } public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getRideId() { return rideId; } public void setRideId(String rideId) { this.rideId = rideId; }
    public Instant getIssuedAt() { return issuedAt; } public void setIssuedAt(Instant issuedAt) { this.issuedAt = issuedAt; }
}
