package com.ridelink.payment.repository;
import com.ridelink.payment.model.Receipt;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ReceiptRepository extends MongoRepository<Receipt, String> {
	Optional<Receipt> findByReceiptNumber(String receiptNumber);
}
