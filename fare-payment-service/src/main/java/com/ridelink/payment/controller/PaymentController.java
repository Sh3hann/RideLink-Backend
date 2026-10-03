package com.ridelink.payment.controller;

import com.ridelink.payment.dto.ProcessPaymentRequest;
import com.ridelink.payment.model.Payment;
import com.ridelink.payment.repository.PaymentRepository;
import com.ridelink.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("/process")
    public Payment process(@RequestBody ProcessPaymentRequest request) {
        return paymentService.processPayment(request);
    }

    @GetMapping("/{id}")
    public Payment getById(@PathVariable String id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    }

    @GetMapping("/ride/{rideId}")
    public List<Payment> getByRideId(@PathVariable String rideId) {
        return paymentRepository.findByRideId(rideId);
    }
}