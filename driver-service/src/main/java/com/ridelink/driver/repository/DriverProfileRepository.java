package com.ridelink.driver.repository;

import com.ridelink.driver.model.DriverProfile;
import com.ridelink.driver.model.VehicleClass;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverProfileRepository extends MongoRepository<DriverProfile, String> {
    Optional<DriverProfile> findByUserId(String userId);

    @Query("{ 'serviceArea': ?0, 'availabilityStatus': 'AVAILABLE' }")
    List<DriverProfile> findAvailableInArea(String serviceArea);

    @Query("{ 'serviceArea': ?0, 'availabilityStatus': 'AVAILABLE', 'vehicle.vehicleClass': ?1 }")
    List<DriverProfile> findEligibleAvailableDrivers(String serviceArea, VehicleClass vehicleClass);
}
