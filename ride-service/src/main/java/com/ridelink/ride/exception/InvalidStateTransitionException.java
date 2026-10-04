package com.ridelink.ride.exception;

import com.ridelink.ride.model.RideStatus;

import java.util.Set;

public class InvalidStateTransitionException extends RuntimeException {
    private final RideStatus currentStatus;
    private final RideStatus targetStatus;

    public InvalidStateTransitionException(RideStatus currentStatus, RideStatus targetStatus, Set<RideStatus> allowedTargets) {
        super(String.format("Invalid ride status transition from '%s' to '%s'. Allowed transitions from '%s' are: %s",
                currentStatus, targetStatus, currentStatus, allowedTargets));
        this.currentStatus = currentStatus;
        this.targetStatus = targetStatus;
    }

    public RideStatus getCurrentStatus() {
        return currentStatus;
    }

    public RideStatus getTargetStatus() {
        return targetStatus;
    }
}
