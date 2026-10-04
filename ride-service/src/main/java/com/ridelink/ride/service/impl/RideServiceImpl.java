package com.ridelink.ride.service.impl;

import com.ridelink.ride.client.DriverServiceClient;
import com.ridelink.ride.client.FareServiceClient;
import com.ridelink.ride.dto.*;
import com.ridelink.ride.exception.RideNotFoundException;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.repository.RideRepository;
import com.ridelink.ride.service.RideService;
import com.ridelink.ride.service.RideStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RideServiceImpl implements RideService {

    private static final Logger log = LoggerFactory.getLogger(RideServiceImpl.class);

    private final RideRepository rideRepository;
    private final RideStateMachine stateMachine;
    private final DriverServiceClient driverClient;
    private final FareServiceClient fareClient;

    public RideServiceImpl(RideRepository rideRepository,
                           RideStateMachine stateMachine,
                           DriverServiceClient driverClient,
                           FareServiceClient fareClient) {
        this.rideRepository = rideRepository;
        this.stateMachine = stateMachine;
        this.driverClient = driverClient;
        this.fareClient = fareClient;
    }

    @Override
    public Ride createRide(CreateRideRequest req) {
        log.info("Processing ride request for passenger: {} in area: {}", req.getPassengerId(), req.getServiceArea());

        Ride ride = new Ride(
                req.getPassengerId(),
                req.getPickupLocation(),
                req.getDestinationLocation(),
                req.getServiceArea(),
                req.getVehicleClass(),
                req.getDistanceKm(),
                req.getEstimatedDurationMin()
        );
        ride.setNotes(req.getNotes());
        ride.setStatus(RideStatus.REQUESTED);
        ride.setRequestedAt(Instant.now());

        // Interservice Interaction 1: Request fare estimation from Fare & Payment Service (MS4)
        double estimatedFare = calculateEstimatedFare(req);
        ride.setEstimatedFare(estimatedFare);
        ride.setFinalFare(estimatedFare);

        // Save initial ride record in MongoDB persistence boundary
        Ride savedRide = rideRepository.save(ride);
        log.info("Created ride entity with ID: {}, estimated fare: LKR {}", savedRide.getId(), estimatedFare);

        // Interservice Interaction 2: Match and assign eligible driver via Driver & Vehicle Service (MS2)
        try {
            List<DriverProfileDto> eligibleDrivers = driverClient.getEligibleDrivers(
                    req.getServiceArea(),
                    req.getVehicleClass()
            );

            if (eligibleDrivers != null && !eligibleDrivers.isEmpty()) {
                DriverProfileDto assignedDriver = eligibleDrivers.get(0);
                log.info("Found eligible driver: {} for ride: {}", assignedDriver.getId(), savedRide.getId());

                stateMachine.validateTransition(savedRide.getStatus(), RideStatus.ASSIGNED);
                savedRide.setDriverId(assignedDriver.getId());
                savedRide.setStatus(RideStatus.ASSIGNED);
                savedRide.setAssignedAt(Instant.now());

                // Notify Driver Service of active assignment
                try {
                    driverClient.setActiveRide(assignedDriver.getId(), savedRide.getId());
                } catch (Exception ex) {
                    log.warn("Could not notify driver service of active ride: {}", ex.getMessage());
                }
            } else {
                log.warn("No eligible driver currently available in area: {}. Ride {} remains REQUESTED.",
                        req.getServiceArea(), savedRide.getId());
            }
        } catch (Exception ex) {
            log.warn("Driver Service unreachable or returned error during driver search: {}. Ride remains in REQUESTED status.",
                    ex.getMessage());
        }

        return rideRepository.save(savedRide);
    }

    @Override
    public Ride assignDriver(String rideId, String driverId) {
        Ride ride = getRideEntity(rideId);
        stateMachine.validateTransition(ride.getStatus(), RideStatus.ASSIGNED);

        ride.setDriverId(driverId);
        ride.setStatus(RideStatus.ASSIGNED);
        ride.setAssignedAt(Instant.now());

        try {
            driverClient.setActiveRide(driverId, ride.getId());
        } catch (Exception ex) {
            log.warn("Failed to set active ride on Driver Service for driver {}: {}", driverId, ex.getMessage());
        }

        return rideRepository.save(ride);
    }

    @Override
    public Ride acceptRide(String rideId, String driverId) {
        Ride ride = getRideEntity(rideId);
        stateMachine.validateTransition(ride.getStatus(), RideStatus.ACCEPTED);

        if (driverId != null && ride.getDriverId() != null && !driverId.equals(ride.getDriverId())) {
            throw new IllegalArgumentException(String.format("Driver ID '%s' does not match assigned driver '%s'", driverId, ride.getDriverId()));
        }
        if (ride.getDriverId() == null && driverId != null) {
            ride.setDriverId(driverId);
        }

        ride.setStatus(RideStatus.ACCEPTED);
        ride.setAcceptedAt(Instant.now());
        log.info("Ride {} accepted by driver {}", rideId, ride.getDriverId());
        return rideRepository.save(ride);
    }

    @Override
    public Ride startRide(String rideId, String driverId) {
        Ride ride = getRideEntity(rideId);
        stateMachine.validateTransition(ride.getStatus(), RideStatus.IN_PROGRESS);

        if (driverId != null && ride.getDriverId() != null && !driverId.equals(ride.getDriverId())) {
            throw new IllegalArgumentException(String.format("Driver ID '%s' does not match assigned driver '%s'", driverId, ride.getDriverId()));
        }

        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setStartedAt(Instant.now());
        log.info("Ride {} started and is now IN_PROGRESS", rideId);
        return rideRepository.save(ride);
    }

    @Override
    public Ride completeRide(String rideId, String driverId) {
        Ride ride = getRideEntity(rideId);
        stateMachine.validateTransition(ride.getStatus(), RideStatus.COMPLETED);

        if (driverId != null && ride.getDriverId() != null && !driverId.equals(ride.getDriverId())) {
            throw new IllegalArgumentException(String.format("Driver ID '%s' does not match assigned driver '%s'", driverId, ride.getDriverId()));
        }

        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(Instant.now());

        // Interservice call: Calculate final fare or confirm with Fare Service
        try {
            Map<String, Object> finalReq = new HashMap<>();
            finalReq.put("rideId", ride.getId());
            finalReq.put("distanceKm", ride.getDistanceKm());
            finalReq.put("actualDurationMin", ride.getEstimatedDurationMin());
            finalReq.put("vehicleClass", ride.getVehicleClass());

            FareEstimateDto finalEstimate = fareClient.calculateFinalFare(finalReq);
            if (finalEstimate != null && finalEstimate.getTotalEstimatedFare() > 0) {
                ride.setFinalFare(finalEstimate.getTotalEstimatedFare());
            } else {
                ride.setFinalFare(ride.getEstimatedFare());
            }
        } catch (Exception ex) {
            log.warn("Fare Service calculation unavailable at completion: {}. Using upfront estimate: LKR {}",
                    ex.getMessage(), ride.getEstimatedFare());
            ride.setFinalFare(ride.getEstimatedFare());
        }

        // Release driver active ride and restore driver availability
        if (ride.getDriverId() != null) {
            try {
                driverClient.setActiveRide(ride.getDriverId(), null);
                driverClient.updateAvailability(ride.getDriverId(), true);
            } catch (Exception ex) {
                log.warn("Failed to release driver {} in Driver Service: {}", ride.getDriverId(), ex.getMessage());
            }
        }

        log.info("Ride {} successfully COMPLETED. Final fare: LKR {}", rideId, ride.getFinalFare());
        return rideRepository.save(ride);
    }

    @Override
    public Ride cancelRide(String rideId, CancelRideRequest cancelReq) {
        Ride ride = getRideEntity(rideId);
        stateMachine.validateTransition(ride.getStatus(), RideStatus.CANCELLED);

        ride.setStatus(RideStatus.CANCELLED);
        ride.setCancelledAt(Instant.now());
        ride.setCancellationReason(cancelReq != null ? cancelReq.getCancellationReason() : "Cancelled by user");
        ride.setCancelledBy(cancelReq != null && cancelReq.getCancelledBy() != null ? cancelReq.getCancelledBy() : "PASSENGER");

        // Release driver if assigned
        if (ride.getDriverId() != null) {
            try {
                driverClient.setActiveRide(ride.getDriverId(), null);
                driverClient.updateAvailability(ride.getDriverId(), true);
            } catch (Exception ex) {
                log.warn("Failed to release driver on cancellation: {}", ex.getMessage());
            }
        }

        log.info("Ride {} CANCELLED. Reason: {}", rideId, ride.getCancellationReason());
        return rideRepository.save(ride);
    }

    @Override
    public Ride updateRideStatus(String rideId, UpdateRideStatusRequest req) {
        if (req == null || req.getStatus() == null) {
            throw new IllegalArgumentException("Target status must not be null");
        }

        return switch (req.getStatus()) {
            case ASSIGNED -> assignDriver(rideId, req.getDriverId());
            case ACCEPTED -> acceptRide(rideId, req.getDriverId());
            case IN_PROGRESS -> startRide(rideId, req.getDriverId());
            case COMPLETED -> completeRide(rideId, req.getDriverId());
            case CANCELLED -> cancelRide(rideId, new CancelRideRequest(req.getCancellationReason(), req.getUpdatedBy()));
            case REQUESTED -> {
                Ride ride = getRideEntity(rideId);
                stateMachine.validateTransition(ride.getStatus(), RideStatus.REQUESTED);
                ride.setStatus(RideStatus.REQUESTED);
                ride.setDriverId(null);
                yield rideRepository.save(ride);
            }
        };
    }

    @Override
    public Ride getRideById(String rideId) {
        return getRideEntity(rideId);
    }

    @Override
    public List<Ride> getAllRides(RideStatus status) {
        if (status != null) {
            return rideRepository.findByStatus(status);
        }
        return rideRepository.findAll();
    }

    @Override
    public List<Ride> getRidesByPassenger(String passengerId) {
        return rideRepository.findByPassengerId(passengerId);
    }

    @Override
    public List<Ride> getRidesByDriver(String driverId) {
        return rideRepository.findByDriverId(driverId);
    }

    private Ride getRideEntity(String rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(rideId));
    }

    private double calculateEstimatedFare(CreateRideRequest req) {
        try {
            Map<String, Object> fareReq = new HashMap<>();
            fareReq.put("passengerId", req.getPassengerId());
            fareReq.put("distanceKm", req.getDistanceKm());
            fareReq.put("estimatedDurationMin", req.getEstimatedDurationMin());
            fareReq.put("vehicleClass", req.getVehicleClass());
            fareReq.put("serviceArea", req.getServiceArea());

            FareEstimateDto estimate = fareClient.getFareEstimate(fareReq);
            if (estimate != null && estimate.getTotalEstimatedFare() > 0) {
                return estimate.getTotalEstimatedFare();
            }
        } catch (Exception ex) {
            log.warn("Fare Service unavailable: {}. Applying default calculation formula.", ex.getMessage());
        }

        // Standard Fallback Pricing Formula: Base 150 + (Distance * 80) + (Duration * 10)
        double base = 150.0;
        double perKm = switch (req.getVehicleClass() != null ? req.getVehicleClass().toUpperCase() : "CAR_SEDAN") {
            case "TUK", "BIKE" -> 50.0;
            case "VAN" -> 120.0;
            default -> 80.0;
        };
        return Math.round((base + (req.getDistanceKm() * perKm) + (req.getEstimatedDurationMin() * 10.0)) * 100.0) / 100.0;
    }
}
