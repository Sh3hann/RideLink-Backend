package com.ridelink.ride.exception;

public class RideServiceException extends RuntimeException {
    public RideServiceException(String message) {
        super(message);
    }

    public RideServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
