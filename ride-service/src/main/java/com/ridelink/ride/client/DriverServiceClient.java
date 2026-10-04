package com.ridelink.ride.client;

import com.ridelink.ride.dto.DriverProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "driver-service", url = "${services.driver-service.url:http://localhost:8082}")
public interface DriverServiceClient {

    @GetMapping("/api/v1/drivers/eligible")
    List<DriverProfileDto> getEligibleDrivers(
            @RequestParam("serviceArea") String serviceArea,
            @RequestParam("vehicleClass") String vehicleClass
    );

    @GetMapping("/api/v1/drivers/{driverId}")
    DriverProfileDto getDriverProfile(@PathVariable("driverId") String driverId);

    @PostMapping("/api/v1/drivers/{driverId}/active-ride")
    void setActiveRide(
            @PathVariable("driverId") String driverId,
            @RequestParam(value = "rideId", required = false) String rideId
    );

    @PostMapping("/api/v1/drivers/{driverId}/availability")
    void updateAvailability(
            @PathVariable("driverId") String driverId,
            @RequestParam("available") boolean available
    );
}
