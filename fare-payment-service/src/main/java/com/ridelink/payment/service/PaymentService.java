package com.ridelink.payment.service;

import com.ridelink.payment.dto.ProcessPaymentRequest;
import com.ridelink.payment.model.*;
import com.ridelink.payment.repository.PaymentRepository;
import com.ridelink.payment.repository.ReceiptRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReceiptRepository receiptRepository;

    public PaymentService(PaymentRepository paymentRepository, ReceiptRepository receiptRepository) {
        this.paymentRepository = paymentRepository;
        this.receiptRepository = receiptRepository;
    }

    public Payment processPayment(ProcessPaymentRequest req) {
        Payment payment = new Payment();
        payment.setRideId(req.getRideId());
        payment.setPassengerId(req.getPassengerId());
        payment.setDriverId(req.getDriverId());
        payment.setAmount(req.getAmount());
        payment.setCurrency("LKR");
        payment.setPaymentMethod(req.getPaymentMethod());
        payment.setProcessedAt(Instant.now());

        if (req.isSimulateFailure()) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.setFailureReason("SIMULATED_DECLINE: Insufficient funds or gateway timeout");
            payment.setTransactionRef("FAIL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            return paymentRepository.save(payment);
        }

        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setTransactionRef("TXN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        Payment saved = paymentRepository.save(payment);

        // Generate Itemized Receipt
        Receipt receipt = new Receipt();
        receipt.setReceiptNumber("RL-REC-2026-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        receipt.setPaymentId(saved.getId());
        receipt.setRideId(saved.getRideId());
        receipt.setIssuedAt(Instant.now());
        receiptRepository.save(receipt);

        return saved;
    }
}