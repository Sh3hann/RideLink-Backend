package com.ridelink.driver.controller;

import com.ridelink.driver.dto.UpdateAvailabilityRequest;
import com.ridelink.driver.model.AvailabilityStatus;
import com.ridelink.driver.model.DriverProfile;
import com.ridelink.driver.model.VehicleClass;
import com.ridelink.driver.service.DriverService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/v1/drivers")
public class DriverInternalController {

    private final DriverService driverService;

    public DriverInternalController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/eligible")
    public ResponseEntity<List<DriverProfile>> getEligibleDrivers(
            @RequestParam String serviceArea,
            @RequestParam(required = false) String vehicleClass) {
        VehicleClass vClass = vehicleClass != null ? VehicleClass.valueOf(vehicleClass.trim().toUpperCase()) : null;
        return ResponseEntity.ok(driverService.getEligibleAvailableDrivers(serviceArea, vClass));
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<DriverProfile> getDriverProfile(@PathVariable String driverId) {
        return driverService.getDriverById(driverId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{driverId}/active-ride")
    public ResponseEntity<DriverProfile> setActiveRide(
            @PathVariable String driverId,
            @RequestParam(required = false) String rideId) {
        return ResponseEntity.ok(driverService.setAssignedRide(driverId, rideId));
    }

    @PostMapping("/{driverId}/availability")
    public ResponseEntity<DriverProfile> updateAvailability(
            @PathVariable String driverId,
            @RequestParam boolean available) {
        UpdateAvailabilityRequest req = new UpdateAvailabilityRequest();
        req.setStatus(available ? AvailabilityStatus.AVAILABLE : AvailabilityStatus.OFFLINE);
        return ResponseEntity.ok(driverService.updateAvailability(driverId, req));
    }
}
