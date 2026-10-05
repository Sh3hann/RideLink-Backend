package com.ridelink.payment.controller;

import com.ridelink.payment.dto.FareEstimateRequest;
import com.ridelink.payment.dto.FareEstimateResponse;
import com.ridelink.payment.service.FareService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fares")
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    @PostMapping("/estimate")
    public FareEstimateResponse estimate(@RequestBody FareEstimateRequest request) {
        return fareService.calculateEstimate(request);
    }

    @PostMapping("/calculate-final")
    public FareEstimateResponse calculateFinalFare(@RequestBody FareEstimateRequest request) {
        return fareService.calculateEstimate(request);
    }
}
