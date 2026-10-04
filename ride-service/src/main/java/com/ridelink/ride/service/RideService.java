package com.ridelink.ride.service;

import com.ridelink.ride.dto.CancelRideRequest;
import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.dto.UpdateRideStatusRequest;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;

import java.util.List;

/**
 * RideService Interface following SOLID Dependency Inversion Principle (DIP).
 */
public interface RideService {

    Ride createRide(CreateRideRequest req);

    Ride assignDriver(String rideId, String driverId);

    Ride acceptRide(String rideId, String driverId);

    Ride startRide(String rideId, String driverId);

    Ride completeRide(String rideId, String driverId);

    Ride cancelRide(String rideId, CancelRideRequest cancelReq);

    Ride updateRideStatus(String rideId, UpdateRideStatusRequest req);

    Ride getRideById(String rideId);

    List<Ride> getAllRides(RideStatus status);

    List<Ride> getRidesByPassenger(String passengerId);

    List<Ride> getRidesByDriver(String driverId);
}
