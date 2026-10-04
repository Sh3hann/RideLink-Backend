package com.ridelink.ride.exception;

public class NoDriverAvailableException extends RuntimeException {
    public NoDriverAvailableException(String serviceArea, String vehicleClass) {
        super(String.format("No eligible available driver found in service area '%s' for vehicle class '%s'", serviceArea, vehicleClass));
    }
}
