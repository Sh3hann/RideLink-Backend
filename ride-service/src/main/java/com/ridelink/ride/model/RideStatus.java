package com.ridelink.ride.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Lifecycle status states for a RideLink ride")
public enum RideStatus {
    @Schema(description = "Ride requested by passenger, awaiting driver assignment")
    REQUESTED,

    @Schema(description = "Driver matched and assigned, awaiting driver acceptance")
    ASSIGNED,

    @Schema(description = "Driver accepted the ride and is en route to pickup")
    ACCEPTED,

    @Schema(description = "Passenger picked up, ride currently in progress")
    IN_PROGRESS,

    @Schema(description = "Ride completed and final fare recorded")
    COMPLETED,

    @Schema(description = "Ride cancelled by passenger, driver, or system")
    CANCELLED
}
