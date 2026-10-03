package com.ridelink.payment.repository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ridelink.payment.model.Payment;

public interface PaymentRepository extends MongoRepository<Payment, String> {
	List<Payment> findByRideId(String rideId);
}
