package com.ridelink.ride.repository;

import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends MongoRepository<Ride, String> {

    List<Ride> findByPassengerId(String passengerId);

    List<Ride> findByDriverId(String driverId);

    List<Ride> findByStatus(RideStatus status);

    List<Ride> findByPassengerIdAndStatus(String passengerId, RideStatus status);

    List<Ride> findByDriverIdAndStatus(String driverId, RideStatus status);

    List<Ride> findByServiceArea(String serviceArea);

    Optional<Ride> findTopByPassengerIdOrderByRequestedAtDesc(String passengerId);

    Optional<Ride> findTopByDriverIdOrderByAssignedAtDesc(String driverId);
}
