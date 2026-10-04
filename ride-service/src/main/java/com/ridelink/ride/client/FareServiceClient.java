package com.ridelink.ride.client;

import com.ridelink.ride.dto.FareEstimateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "fare-payment-service", url = "${services.fare-service.url:http://localhost:8084}")
public interface FareServiceClient {

    @PostMapping("/api/v1/fares/estimate")
    FareEstimateDto getFareEstimate(@RequestBody Map<String, Object> req);

    @PostMapping("/api/v1/fares/calculate-final")
    FareEstimateDto calculateFinalFare(@RequestBody Map<String, Object> req);
}
