package com.ridelink.driver.service;

import com.ridelink.driver.dto.*;
import com.ridelink.driver.model.*;
import com.ridelink.driver.repository.DriverProfileRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class DriverService {

    private final DriverProfileRepository repository;

    public DriverService(DriverProfileRepository repository) {
        this.repository = repository;
    }

    public DriverProfile registerProfile(DriverProfileRequest req) {
        if (repository.findByUserId(req.getUserId()).isPresent()) {
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
                .orElseThrow(() -> new IllegalArgumentException("Driver profile not found: " + driverId));
        profile.setAvailabilityStatus(req.getStatus());
        profile.setUpdatedAt(Instant.now());
        return repository.save(profile);
    }

    public List<DriverProfile> getEligibleAvailableDrivers(String serviceArea, VehicleClass vehicleClass) {
        if (vehicleClass != null) {
            return repository.findEligibleAvailableDrivers(serviceArea, vehicleClass);
        }
        return repository.findAvailableInArea(serviceArea);
    }

    public DriverProfile setAssignedRide(String driverId, String rideId) {
        DriverProfile profile = repository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver profile not found: " + driverId));
        profile.setActiveRideId(rideId);
        profile.setAvailabilityStatus(rideId != null ? AvailabilityStatus.BUSY : AvailabilityStatus.AVAILABLE);
        profile.setUpdatedAt(Instant.now());
        return repository.save(profile);
    }
}