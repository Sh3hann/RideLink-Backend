package com.ridelink.driver.controller;

import com.ridelink.driver.dto.DriverProfileRequest;
import com.ridelink.driver.dto.UpdateAvailabilityRequest;
import com.ridelink.driver.model.DriverProfile;
import com.ridelink.driver.model.VehicleClass;
import com.ridelink.driver.service.DriverService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private static final Logger logger = LoggerFactory.getLogger(DriverController.class);
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<DriverProfile> registerProfile(@Valid @RequestBody DriverProfileRequest req) {
        logger.info("Received request to register driver profile for userId: {}", req.getUserId());
        DriverProfile profile = driverService.registerProfile(req);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<DriverProfile> getDriverProfile(@PathVariable String driverId) {
        logger.info("Received request to get driver profile for driverId: {}", driverId);
        return driverService.getDriverById(driverId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{driverId}/availability")
    public ResponseEntity<DriverProfile> updateAvailability(
            @PathVariable String driverId,
            @Valid @RequestBody UpdateAvailabilityRequest req) {
        logger.info("Received request to update availability for driverId: {} to {}", driverId, req.getStatus());
        DriverProfile profile = driverService.updateAvailability(driverId, req);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/eligible")
    public ResponseEntity<List<DriverProfile>> getEligibleAvailableDrivers(
            @RequestParam String serviceArea,
            @RequestParam(required = false) String vehicleClass) {
        logger.info("Fetching eligible available drivers in area: {}, class: {}", serviceArea, vehicleClass);
        VehicleClass vClass = vehicleClass != null ? VehicleClass.valueOf(vehicleClass.trim().toUpperCase()) : null;
        List<DriverProfile> drivers = driverService.getEligibleAvailableDrivers(serviceArea, vClass);
        return ResponseEntity.ok(drivers);
    }

    @PostMapping("/{driverId}/active-ride")
    public ResponseEntity<DriverProfile> setActiveRide(
            @PathVariable String driverId,
            @RequestParam(required = false) String rideId) {
        logger.info("Received request to set active ride for driverId: {} to rideId: {}", driverId, rideId);
        DriverProfile profile = driverService.setAssignedRide(driverId, rideId);
        return ResponseEntity.ok(profile);
    }
}
