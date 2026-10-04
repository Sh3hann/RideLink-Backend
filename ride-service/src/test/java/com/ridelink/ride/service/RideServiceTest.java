package com.ridelink.ride.service;

import com.ridelink.ride.client.DriverServiceClient;
import com.ridelink.ride.client.FareServiceClient;
import com.ridelink.ride.dto.CancelRideRequest;
import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.dto.DriverProfileDto;
import com.ridelink.ride.dto.FareEstimateDto;
import com.ridelink.ride.dto.UpdateRideStatusRequest;
import com.ridelink.ride.exception.InvalidStateTransitionException;
import com.ridelink.ride.exception.RideNotFoundException;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.repository.RideRepository;
import com.ridelink.ride.service.impl.RideServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RideService Business Logic Unit Tests")
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverServiceClient driverClient;

    @Mock
    private FareServiceClient fareClient;

    private RideStateMachine stateMachine;
    private RideService rideService;

    @BeforeEach
    void setUp() {
        stateMachine = new RideStateMachine();
        rideService = new RideServiceImpl(rideRepository, stateMachine, driverClient, fareClient);
    }

    @Test
    @DisplayName("Should create ride, get fare estimate, and assign eligible driver (Happy Path)")
    void testCreateRide_WithEligibleDriver_Success() {
        CreateRideRequest request = new CreateRideRequest(
                "usr_pass_101",
                "Colombo Fort",
                "Galle Face",
                "COLOMBO_CENTRAL",
                "CAR_SEDAN",
                5.0,
                15.0
        );

        FareEstimateDto fareEstimate = new FareEstimateDto(550.0);
        when(fareClient.getFareEstimate(anyMap())).thenReturn(fareEstimate);

        DriverProfileDto driver = new DriverProfileDto("drv_1", "Kasun Perera", "COLOMBO_CENTRAL", "CAR_SEDAN");
        when(driverClient.getEligibleDrivers("COLOMBO_CENTRAL", "CAR_SEDAN")).thenReturn(List.of(driver));

        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> {
            Ride r = invocation.getArgument(0);
            if (r.getId() == null) {
                r.setId("ride_123");
            }
            return r;
        });

        Ride created = rideService.createRide(request);

        assertNotNull(created);
        assertEquals("usr_pass_101", created.getPassengerId());
        assertEquals("drv_1", created.getDriverId());
        assertEquals(RideStatus.ASSIGNED, created.getStatus());
        assertEquals(550.0, created.getEstimatedFare());
        assertNotNull(created.getRequestedAt());
        assertNotNull(created.getAssignedAt());

        verify(driverClient).setActiveRide("drv_1", "ride_123");
        verify(rideRepository, atLeastOnce()).save(any(Ride.class));
    }

    @Test
    @DisplayName("Should create ride in REQUESTED status when no drivers are available (Degraded Mode)")
    void testCreateRide_WhenNoDriverAvailable_RemainsRequested() {
        CreateRideRequest request = new CreateRideRequest(
                "usr_pass_102",
                "Kandy Town",
                "Peradeniya",
                "KANDY_CENTRAL",
                "CAR_SEDAN",
                6.0,
                20.0
        );

        FareEstimateDto fareEstimate = new FareEstimateDto(700.0);
        when(fareClient.getFareEstimate(anyMap())).thenReturn(fareEstimate);
        when(driverClient.getEligibleDrivers("KANDY_CENTRAL", "CAR_SEDAN")).thenReturn(Collections.emptyList());

        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> {
            Ride r = invocation.getArgument(0);
            r.setId("ride_124");
            return r;
        });

        Ride created = rideService.createRide(request);

        assertNotNull(created);
        assertNull(created.getDriverId());
        assertEquals(RideStatus.REQUESTED, created.getStatus());
        verify(driverClient, never()).setActiveRide(anyString(), anyString());
    }

    @Test
    @DisplayName("Should calculate fallback fare when Fare Service is down or throws exception")
    void testCreateRide_WhenFareServiceFails_UsesFallbackFormula() {
        CreateRideRequest request = new CreateRideRequest(
                "usr_pass_103",
                "Nugegoda",
                "Maharagama",
                "COLOMBO_SUB",
                "CAR_SEDAN",
                4.0,
                10.0
        );

        when(fareClient.getFareEstimate(anyMap())).thenThrow(new RuntimeException("Fare service 503 Unavailable"));
        when(driverClient.getEligibleDrivers(anyString(), anyString())).thenReturn(Collections.emptyList());

        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ride created = rideService.createRide(request);

        assertNotNull(created);
        // Base 150 + (4.0 * 80) + (10.0 * 10) = 150 + 320 + 100 = 570.0
        assertEquals(570.0, created.getEstimatedFare());
    }

    @Test
    @DisplayName("Should allow driver to accept assigned ride (ASSIGNED -> ACCEPTED)")
    void testAcceptRide_Success() {
        Ride ride = new Ride();
        ride.setId("ride_001");
        ride.setDriverId("drv_1");
        ride.setStatus(RideStatus.ASSIGNED);

        when(rideRepository.findById("ride_001")).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ride accepted = rideService.acceptRide("ride_001", "drv_1");

        assertEquals(RideStatus.ACCEPTED, accepted.getStatus());
        assertNotNull(accepted.getAcceptedAt());
    }

    @Test
    @DisplayName("Should reject acceptRide when driver ID does not match assigned driver")
    void testAcceptRide_DriverMismatch_ThrowsException() {
        Ride ride = new Ride();
        ride.setId("ride_001");
        ride.setDriverId("drv_1");
        ride.setStatus(RideStatus.ASSIGNED);

        when(rideRepository.findById("ride_001")).thenReturn(Optional.of(ride));

        assertThrows(IllegalArgumentException.class, () -> rideService.acceptRide("ride_001", "drv_imposter"));
    }

    @Test
    @DisplayName("Should allow driver to start ride (ACCEPTED -> IN_PROGRESS)")
    void testStartRide_Success() {
        Ride ride = new Ride();
        ride.setId("ride_001");
        ride.setDriverId("drv_1");
        ride.setStatus(RideStatus.ACCEPTED);

        when(rideRepository.findById("ride_001")).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ride inProgress = rideService.startRide("ride_001", "drv_1");

        assertEquals(RideStatus.IN_PROGRESS, inProgress.getStatus());
        assertNotNull(inProgress.getStartedAt());
    }

    @Test
    @DisplayName("Should complete ride, calculate final fare, and release driver (IN_PROGRESS -> COMPLETED)")
    void testCompleteRide_Success() {
        Ride ride = new Ride();
        ride.setId("ride_001");
        ride.setDriverId("drv_1");
        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setEstimatedFare(500.0);
        ride.setDistanceKm(5.0);
        ride.setEstimatedDurationMin(15.0);
        ride.setVehicleClass("CAR_SEDAN");

        when(rideRepository.findById("ride_001")).thenReturn(Optional.of(ride));
        when(fareClient.calculateFinalFare(anyMap())).thenReturn(new FareEstimateDto(520.0));
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ride completed = rideService.completeRide("ride_001", "drv_1");

        assertEquals(RideStatus.COMPLETED, completed.getStatus());
        assertEquals(520.0, completed.getFinalFare());
        assertNotNull(completed.getCompletedAt());

        verify(driverClient).setActiveRide("drv_1", null);
        verify(driverClient).updateAvailability("drv_1", true);
    }

    @Test
    @DisplayName("Should cancel ride in ASSIGNED status and release assigned driver")
    void testCancelRide_WhenAssigned_ReleasesDriver() {
        Ride ride = new Ride();
        ride.setId("ride_001");
        ride.setDriverId("drv_1");
        ride.setStatus(RideStatus.ASSIGNED);

        when(rideRepository.findById("ride_001")).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CancelRideRequest cancelReq = new CancelRideRequest("Change of plans", "PASSENGER");
        Ride cancelled = rideService.cancelRide("ride_001", cancelReq);

        assertEquals(RideStatus.CANCELLED, cancelled.getStatus());
        assertEquals("Change of plans", cancelled.getCancellationReason());
        assertEquals("PASSENGER", cancelled.getCancelledBy());
        assertNotNull(cancelled.getCancelledAt());

        verify(driverClient).setActiveRide("drv_1", null);
        verify(driverClient).updateAvailability("drv_1", true);
    }

    @Test
    @DisplayName("Should throw InvalidStateTransitionException when attempting illegal transition (REQUESTED -> COMPLETED)")
    void testCompleteRide_FromRequested_ThrowsInvalidStateTransition() {
        Ride ride = new Ride();
        ride.setId("ride_001");
        ride.setStatus(RideStatus.REQUESTED);

        when(rideRepository.findById("ride_001")).thenReturn(Optional.of(ride));

        assertThrows(InvalidStateTransitionException.class, () -> rideService.completeRide("ride_001", "drv_1"));
    }

    @Test
    @DisplayName("Should throw RideNotFoundException when ride does not exist in MongoDB")
    void testGetRideById_NotFound_ThrowsException() {
        when(rideRepository.findById("non_existent")).thenReturn(Optional.empty());

        assertThrows(RideNotFoundException.class, () -> rideService.getRideById("non_existent"));
    }

    @Test
    @DisplayName("Should correctly dispatch generic updateRideStatus")
    void testUpdateRideStatus_Dispatch() {
        Ride ride = new Ride();
        ride.setId("ride_001");
        ride.setDriverId("drv_1");
        ride.setStatus(RideStatus.ASSIGNED);

        when(rideRepository.findById("ride_001")).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateRideStatusRequest req = new UpdateRideStatusRequest(RideStatus.ACCEPTED);
        req.setDriverId("drv_1");

        Ride result = rideService.updateRideStatus("ride_001", req);
        assertEquals(RideStatus.ACCEPTED, result.getStatus());
    }
}
