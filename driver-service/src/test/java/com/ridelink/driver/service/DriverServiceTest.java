package com.ridelink.driver.service;

import com.ridelink.driver.dto.DriverProfileRequest;
import com.ridelink.driver.dto.UpdateAvailabilityRequest;
import com.ridelink.driver.model.AvailabilityStatus;
import com.ridelink.driver.model.DriverProfile;
import com.ridelink.driver.model.VehicleClass;
import com.ridelink.driver.repository.DriverProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {

    @Mock
    private DriverProfileRepository repository;

    @InjectMocks
    private DriverService driverService;

    private DriverProfile driverProfile;

    @BeforeEach
    void setUp() {
        driverProfile = new DriverProfile();
        driverProfile.setId("drv123");
        driverProfile.setUserId("user123");
        driverProfile.setAvailabilityStatus(AvailabilityStatus.OFFLINE);
    }

    @Test
    void testRegisterProfile_Success() {
        DriverProfileRequest req = new DriverProfileRequest();
        req.setUserId("user456");
        req.setLicenseNumber("LIC123");

        when(repository.findByUserId(req.getUserId())).thenReturn(Optional.empty());
        when(repository.save(any(DriverProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DriverProfile result = driverService.registerProfile(req);

        assertNotNull(result);
        assertEquals("user456", result.getUserId());
        assertEquals("LIC123", result.getLicenseNumber());
        assertEquals(AvailabilityStatus.OFFLINE, result.getAvailabilityStatus());
    }

    @Test
    void testRegisterProfile_UserAlreadyExists() {
        DriverProfileRequest req = new DriverProfileRequest();
        req.setUserId("user123");

        when(repository.findByUserId(req.getUserId())).thenReturn(Optional.of(driverProfile));

        assertThrows(IllegalArgumentException.class, () -> driverService.registerProfile(req));
        verify(repository, never()).save(any());
    }

    @Test
    void testUpdateAvailability_Success() {
        UpdateAvailabilityRequest req = new UpdateAvailabilityRequest();
        req.setStatus(AvailabilityStatus.AVAILABLE);

        when(repository.findById("drv123")).thenReturn(Optional.of(driverProfile));
        when(repository.save(any(DriverProfile.class))).thenReturn(driverProfile);

        DriverProfile result = driverService.updateAvailability("drv123", req);

        assertNotNull(result);
        assertEquals(AvailabilityStatus.AVAILABLE, result.getAvailabilityStatus());
    }

    @Test
    void testGetEligibleAvailableDrivers() {
        when(repository.findEligibleAvailableDrivers("Downtown", VehicleClass.STANDARD))
                .thenReturn(List.of(driverProfile));

        List<DriverProfile> result = driverService.getEligibleAvailableDrivers("Downtown", VehicleClass.STANDARD);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
