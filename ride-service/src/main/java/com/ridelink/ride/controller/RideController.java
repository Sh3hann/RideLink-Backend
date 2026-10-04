package com.ridelink.ride.controller;

import com.ridelink.ride.dto.*;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/rides")
@Tag(name = "Ride Management API", description = "Endpoints for ride creation, lifecycle state transitions, driver assignments, and ride querying")
@CrossOrigin(origins = "*")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @Operation(summary = "Create a new ride request", description = "Initiates a ride booking, requests upfront fare estimate from Fare Service, persists the ride in MongoDB, and attempts to match an eligible driver from Driver Service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ride successfully created and dispatched",
                    content = @Content(schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or validation failure",
                    content = @Content(schema = @Schema(implementation = com.ridelink.ride.exception.ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody CreateRideRequest request) {
        Ride created = rideService.createRide(request);
        return new ResponseEntity<>(RideResponse.fromEntity(created), HttpStatus.CREATED);
    }

    @Operation(summary = "Alias endpoint for creating ride request", description = "Matches Postman collection route /api/v1/rides/request")
    @PostMapping("/request")
    public ResponseEntity<RideResponse> requestRide(@Valid @RequestBody CreateRideRequest request) {
        return createRide(request);
    }

    @Operation(summary = "Get ride by ID", description = "Retrieves the full ride lifecycle record by its unique MongoDB identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ride found",
                    content = @Content(schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = @Content(schema = @Schema(implementation = com.ridelink.ride.exception.ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getRideById(
            @Parameter(description = "Ride identifier", required = true) @PathVariable("id") String id) {
        Ride ride = rideService.getRideById(id);
        return ResponseEntity.ok(RideResponse.fromEntity(ride));
    }

    @Operation(summary = "List all rides", description = "Retrieves all rides, optionally filtered by lifecycle status.")
    @GetMapping
    public ResponseEntity<List<RideResponse>> getAllRides(
            @Parameter(description = "Filter by status") @RequestParam(value = "status", required = false) RideStatus status) {
        List<RideResponse> responses = rideService.getAllRides(status).stream()
                .map(RideResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get rides by passenger ID", description = "Retrieves all rides requested by a given passenger.")
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<RideResponse>> getRidesByPassenger(
            @Parameter(description = "Passenger identifier", required = true) @PathVariable("passengerId") String passengerId) {
        List<RideResponse> responses = rideService.getRidesByPassenger(passengerId).stream()
                .map(RideResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Alias: Get rides by rider ID", description = "Matches Postman collection route /api/v1/rides/rider/{riderId}")
    @GetMapping("/rider/{riderId}")
    public ResponseEntity<List<RideResponse>> getRidesByRider(
            @Parameter(description = "Rider identifier", required = true) @PathVariable("riderId") String riderId) {
        return getRidesByPassenger(riderId);
    }

    @Operation(summary = "Get rides by driver ID", description = "Retrieves all rides assigned to a given driver.")
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<RideResponse>> getRidesByDriver(
            @Parameter(description = "Driver identifier", required = true) @PathVariable("driverId") String driverId) {
        List<RideResponse> responses = rideService.getRidesByDriver(driverId).stream()
                .map(RideResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Assign a driver to a ride", description = "Assigns an eligible driver to a REQUESTED ride (REQUESTED -> ASSIGNED).")
    @PostMapping("/{id}/assign-driver")
    public ResponseEntity<RideResponse> assignDriver(
            @PathVariable("id") String id,
            @Valid @RequestBody AssignDriverRequest request) {
        Ride updated = rideService.assignDriver(id, request.getDriverId());
        return ResponseEntity.ok(RideResponse.fromEntity(updated));
    }

    @Operation(summary = "Driver accepts ride", description = "Driver accepts an assigned ride (ASSIGNED -> ACCEPTED).")
    @PostMapping("/{id}/accept")
    public ResponseEntity<RideResponse> acceptRide(
            @PathVariable("id") String id,
            @RequestParam(value = "driverId", required = false) String driverId) {
        Ride updated = rideService.acceptRide(id, driverId);
        return ResponseEntity.ok(RideResponse.fromEntity(updated));
    }

    @Operation(summary = "Driver starts ride", description = "Driver starts the trip once passenger is aboard (ACCEPTED -> IN_PROGRESS).")
    @PostMapping("/{id}/start")
    public ResponseEntity<RideResponse> startRide(
            @PathVariable("id") String id,
            @RequestParam(value = "driverId", required = false) String driverId) {
        Ride updated = rideService.startRide(id, driverId);
        return ResponseEntity.ok(RideResponse.fromEntity(updated));
    }

    @Operation(summary = "Driver completes ride", description = "Driver completes the trip (IN_PROGRESS -> COMPLETED). Final fare is calculated and driver is released.")
    @PostMapping("/{id}/complete")
    public ResponseEntity<RideResponse> completeRide(
            @PathVariable("id") String id,
            @RequestParam(value = "driverId", required = false) String driverId) {
        Ride updated = rideService.completeRide(id, driverId);
        return ResponseEntity.ok(RideResponse.fromEntity(updated));
    }

    @Operation(summary = "Cancel ride", description = "Cancels a ride if in a cancellable state (REQUESTED/ASSIGNED/ACCEPTED -> CANCELLED).")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<RideResponse> cancelRide(
            @PathVariable("id") String id,
            @RequestBody(required = false) CancelRideRequest request) {
        Ride updated = rideService.cancelRide(id, request);
        return ResponseEntity.ok(RideResponse.fromEntity(updated));
    }

    @Operation(summary = "Update ride status (Generic Endpoint)", description = "Generic endpoint for transitioning ride status. Matches Postman PUT /api/v1/rides/{id}/status.")
    @PutMapping("/{id}/status")
    public ResponseEntity<RideResponse> updateRideStatus(
            @PathVariable("id") String id,
            @Valid @RequestBody UpdateRideStatusRequest request) {
        Ride updated = rideService.updateRideStatus(id, request);
        return ResponseEntity.ok(RideResponse.fromEntity(updated));
    }
}
