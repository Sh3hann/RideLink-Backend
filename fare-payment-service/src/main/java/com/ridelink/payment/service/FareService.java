package com.ridelink.payment.service;

import com.ridelink.payment.dto.*;
import com.ridelink.payment.model.FareBreakdown;
import org.springframework.stereotype.Service;

@Service
public class FareService {

    private final double baseFare = 150.0;
    private final double ratePerKm = 80.0;
    private final double ratePerMinute = 5.0;

    public FareEstimateResponse calculateEstimate(FareEstimateRequest req) {
        double multiplier = switch (req.getVehicleClass() != null ? req.getVehicleClass().toUpperCase() : "ECONOMY") {
            case "COMFORT" -> 1.2;
            case "XL" -> 1.5;
            case "PREMIUM" -> 2.0;
            default -> 1.0;
        };

        double distanceCost = req.getDistanceKm() * ratePerKm;
        double timeCost = req.getEstimatedDurationMin() * ratePerMinute;
        double subtotal = (baseFare + distanceCost + timeCost) * multiplier;
        double tax = Math.round(subtotal * 0.08 * 100.0) / 100.0; // 8% VAT
        double total = Math.round((subtotal + tax) / 10.0) * 10.0;

        FareBreakdown breakdown = new FareBreakdown(baseFare, distanceCost, timeCost, 1.0, subtotal, tax, total);

        FareEstimateResponse resp = new FareEstimateResponse();
        resp.setDistanceKm(req.getDistanceKm());
        resp.setBreakdown(breakdown);
        resp.setTotalEstimatedFare(total);
        resp.setCurrency("LKR");
        return resp;
    }
}