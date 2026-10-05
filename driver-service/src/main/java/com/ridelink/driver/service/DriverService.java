package com.ridelink.driver.service;

import com.ridelink.driver.dto.*;
import com.ridelink.driver.model.*;
import com.ridelink.driver.repository.DriverProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class DriverService {

    private static final Logger logger = LoggerFactory.getLogger(DriverService.class);
    private final DriverProfileRepository repository;

    public DriverService(DriverProfileRepository repository) {
        this.repository = repository;
    }

    public DriverProfile registerProfile(DriverProfileRequest req) {
        logger.info("Registering new driver profile for user ID: {}", req.getUserId());
        if (repository.findByUserId(req.getUserId()).isPresent()) {
            logger.warn("Registration failed. Profile already exists for user ID: {}", req.getUserId());
            throw new IllegalArgumentException("Driver profile already exists for user ID: " + req.getUserId());
        }

        DriverProfile profile = new DriverProfile();
        profile.setUserId(req.getUserId());
        profile.setLicenseNumber(req.getLicenseNumber());
        profile.setExperienceYears(req.getExperienceYears());
        profile.setServiceArea(req.getServiceArea());
        profile.setVehicle(req.getVehicle());
        profile.setAvailabilityStatus(AvailabilityStatus.OFFLINE);
        profile.setUpdatedAt(Instant.now());

        return repository.save(profile);
    }

    public DriverProfile updateAvailability(String driverId, UpdateAvailabilityRequest req) {
        DriverProfile profile = repository.findById(driverId)
                .orElseThrow(() -> {
                    logger.error("Failed to update availability. Driver profile not found: {}", driverId);
                    return new IllegalArgumentException("Driver profile not found: " + driverId);
                });
        logger.info("Updating availability for driverId: {} from {} to {}", driverId, profile.getAvailabilityStatus(), req.getStatus());
        profile.setAvailabilityStatus(req.getStatus());
        profile.setUpdatedAt(Instant.now());
        return repository.save(profile);
    }

    public List<DriverProfile> getEligibleAvailableDrivers(String serviceArea, VehicleClass vehicleClass) {
        if (vehicleClass != null) {
            logger.info("Querying available drivers in {} for class {}", serviceArea, vehicleClass);
            return repository.findEligibleAvailableDrivers(serviceArea, vehicleClass);
        }
        logger.info("Querying available drivers in {} for all classes", serviceArea);
        return repository.findAvailableInArea(serviceArea);
    }

    public DriverProfile setAssignedRide(String driverId, String rideId) {
        DriverProfile profile = repository.findById(driverId)
                .orElseThrow(() -> {
                    logger.error("Failed to set assigned ride. Driver profile not found: {}", driverId);
                    return new IllegalArgumentException("Driver profile not found: " + driverId);
                });
        logger.info("Setting active ride for driverId: {} to rideId: {}", driverId, rideId);
        profile.setActiveRideId(rideId);
        profile.setAvailabilityStatus(rideId != null ? AvailabilityStatus.BUSY : AvailabilityStatus.AVAILABLE);
        profile.setUpdatedAt(Instant.now());
        return repository.save(profile);
    }

    public java.util.Optional<DriverProfile> getDriverById(String driverId) {
        return repository.findById(driverId);
    }
}