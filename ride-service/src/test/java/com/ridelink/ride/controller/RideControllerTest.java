package com.ridelink.ride.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridelink.ride.dto.CancelRideRequest;
import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.dto.UpdateRideStatusRequest;
import com.ridelink.ride.exception.GlobalExceptionHandler;
import com.ridelink.ride.exception.InvalidStateTransitionException;
import com.ridelink.ride.exception.RideNotFoundException;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RideController MockMvc Unit Tests")
class RideControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RideService rideService;

    @InjectMocks
    private RideController rideController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rideController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/rides - Should return 201 Created on valid ride request")
    void testCreateRide_Success() throws Exception {
        CreateRideRequest request = new CreateRideRequest(
                "usr_pass_101",
                "Colombo Fort",
                "Galle Face",
                "COLOMBO_CENTRAL",
                "CAR_SEDAN",
                5.0,
                15.0
        );

        Ride ride = new Ride("usr_pass_101", "Colombo Fort", "Galle Face", "COLOMBO_CENTRAL", "CAR_SEDAN", 5.0, 15.0);
        ride.setId("ride_999");
        ride.setStatus(RideStatus.ASSIGNED);
        ride.setDriverId("drv_101");
        ride.setEstimatedFare(550.0);

        when(rideService.createRide(any(CreateRideRequest.class))).thenReturn(ride);

        mockMvc.perform(post("/api/v1/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("ride_999"))
                .andExpect(jsonPath("$.passengerId").value("usr_pass_101"))
                .andExpect(jsonPath("$.driverId").value("drv_101"))
                .andExpect(jsonPath("$.status").value("ASSIGNED"))
                .andExpect(jsonPath("$.estimatedFare").value(550.0));
    }

    @Test
    @DisplayName("POST /api/v1/rides - Should return 400 Bad Request on invalid input (validation failure)")
    void testCreateRide_ValidationError() throws Exception {
        // Missing passengerId, pickupLocation, negative distance
        CreateRideRequest invalidRequest = new CreateRideRequest();
        invalidRequest.setDistanceKm(-5.0);

        mockMvc.perform(post("/api/v1/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    @DisplayName("GET /api/v1/rides/{id} - Should return 200 OK when ride exists")
    void testGetRideById_Success() throws Exception {
        Ride ride = new Ride();
        ride.setId("ride_999");
        ride.setPassengerId("usr_pass_101");
        ride.setStatus(RideStatus.ACCEPTED);

        when(rideService.getRideById("ride_999")).thenReturn(ride);

        mockMvc.perform(get("/api/v1/rides/ride_999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("ride_999"))
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }

    @Test
    @DisplayName("GET /api/v1/rides/{id} - Should return 404 Not Found when ride does not exist")
    void testGetRideById_NotFound() throws Exception {
        when(rideService.getRideById("non_existing")).thenThrow(new RideNotFoundException("non_existing"));

        mockMvc.perform(get("/api/v1/rides/non_existing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Ride not found with id: non_existing"));
    }

    @Test
    @DisplayName("POST /api/v1/rides/{id}/accept - Should return 200 OK on successful acceptance")
    void testAcceptRide_Success() throws Exception {
        Ride ride = new Ride();
        ride.setId("ride_999");
        ride.setStatus(RideStatus.ACCEPTED);
        ride.setDriverId("drv_101");

        when(rideService.acceptRide("ride_999", "drv_101")).thenReturn(ride);

        mockMvc.perform(post("/api/v1/rides/ride_999/accept?driverId=drv_101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }

    @Test
    @DisplayName("POST /api/v1/rides/{id}/start - Should return 200 OK on starting ride")
    void testStartRide_Success() throws Exception {
        Ride ride = new Ride();
        ride.setId("ride_999");
        ride.setStatus(RideStatus.IN_PROGRESS);

        when(rideService.startRide("ride_999", "drv_101")).thenReturn(ride);

        mockMvc.perform(post("/api/v1/rides/ride_999/start?driverId=drv_101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("POST /api/v1/rides/{id}/complete - Should return 200 OK on completing ride")
    void testCompleteRide_Success() throws Exception {
        Ride ride = new Ride();
        ride.setId("ride_999");
        ride.setStatus(RideStatus.COMPLETED);
        ride.setFinalFare(650.0);

        when(rideService.completeRide("ride_999", "drv_101")).thenReturn(ride);

        mockMvc.perform(post("/api/v1/rides/ride_999/complete?driverId=drv_101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.finalFare").value(650.0));
    }

    @Test
    @DisplayName("POST /api/v1/rides/{id}/cancel - Should return 200 OK on cancelling ride")
    void testCancelRide_Success() throws Exception {
        Ride ride = new Ride();
        ride.setId("ride_999");
        ride.setStatus(RideStatus.CANCELLED);
        ride.setCancellationReason("Driver delayed");

        when(rideService.cancelRide(eq("ride_999"), any(CancelRideRequest.class))).thenReturn(ride);

        CancelRideRequest cancelReq = new CancelRideRequest("Driver delayed", "PASSENGER");

        mockMvc.perform(post("/api/v1/rides/ride_999/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.cancellationReason").value("Driver delayed"));
    }

    @Test
    @DisplayName("POST /api/v1/rides/{id}/complete - Should return 400 Bad Request on invalid state transition")
    void testInvalidTransition_Returns400() throws Exception {
        when(rideService.completeRide("ride_999", "drv_101"))
                .thenThrow(new InvalidStateTransitionException(RideStatus.REQUESTED, RideStatus.COMPLETED, Set.of(RideStatus.ASSIGNED, RideStatus.CANCELLED)));

        mockMvc.perform(post("/api/v1/rides/ride_999/complete?driverId=drv_101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("PUT /api/v1/rides/{id}/status - Should update status via generic endpoint")
    void testUpdateStatus_Success() throws Exception {
        Ride ride = new Ride();
        ride.setId("ride_999");
        ride.setStatus(RideStatus.ACCEPTED);

        UpdateRideStatusRequest req = new UpdateRideStatusRequest(RideStatus.ACCEPTED);
        req.setDriverId("drv_101");

        when(rideService.updateRideStatus(eq("ride_999"), any(UpdateRideStatusRequest.class))).thenReturn(ride);

        mockMvc.perform(put("/api/v1/rides/ride_999/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }

    @Test
    @DisplayName("GET /api/v1/rides/passenger/{passengerId} - Should return list of passenger rides")
    void testGetRidesByPassenger() throws Exception {
        Ride ride = new Ride();
        ride.setId("ride_1");
        ride.setPassengerId("usr_pass_101");

        when(rideService.getRidesByPassenger("usr_pass_101")).thenReturn(List.of(ride));

        mockMvc.perform(get("/api/v1/rides/passenger/usr_pass_101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("ride_1"));
    }
}
