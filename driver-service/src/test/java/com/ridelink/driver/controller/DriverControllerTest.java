package com.ridelink.driver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridelink.driver.dto.DriverProfileRequest;
import com.ridelink.driver.dto.UpdateAvailabilityRequest;
import com.ridelink.driver.model.AvailabilityStatus;
import com.ridelink.driver.model.DriverProfile;
import com.ridelink.driver.model.Vehicle;
import com.ridelink.driver.model.VehicleClass;
import com.ridelink.driver.service.DriverService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DriverController.class)
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverService driverService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerProfile_ValidRequest_ReturnsOk() throws Exception {
        DriverProfileRequest req = new DriverProfileRequest();
        req.setUserId("usr_1");
        req.setLicenseNumber("LIC123");
        req.setExperienceYears(3);
        req.setServiceArea("Uptown");
        
        Vehicle vehicle = new Vehicle();
        vehicle.setMake("Toyota");
        vehicle.setModel("Camry");
        vehicle.setLicensePlate("XYZ123");
        vehicle.setVehicleClass(VehicleClass.STANDARD);
        req.setVehicle(vehicle);

        DriverProfile profile = new DriverProfile();
        profile.setId("drv_1");
        profile.setUserId("usr_1");

        Mockito.when(driverService.registerProfile(any(DriverProfileRequest.class))).thenReturn(profile);

        mockMvc.perform(post("/api/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("drv_1"));
    }

    @Test
    void registerProfile_InvalidRequest_ReturnsBadRequest() throws Exception {
        DriverProfileRequest req = new DriverProfileRequest(); // empty request

        mockMvc.perform(post("/api/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAvailability_ValidRequest_ReturnsOk() throws Exception {
        UpdateAvailabilityRequest req = new UpdateAvailabilityRequest();
        req.setStatus(AvailabilityStatus.AVAILABLE);

        DriverProfile profile = new DriverProfile();
        profile.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        Mockito.when(driverService.updateAvailability(eq("drv_1"), any(UpdateAvailabilityRequest.class))).thenReturn(profile);

        mockMvc.perform(put("/api/drivers/drv_1/availability")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availabilityStatus").value("AVAILABLE"));
    }

    @Test
    void getEligibleAvailableDrivers_ReturnsList() throws Exception {
        DriverProfile profile = new DriverProfile();
        profile.setId("drv_1");

        Mockito.when(driverService.getEligibleAvailableDrivers("Uptown", VehicleClass.STANDARD))
                .thenReturn(List.of(profile));

        mockMvc.perform(get("/api/drivers/eligible")
                .param("serviceArea", "Uptown")
                .param("vehicleClass", "STANDARD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("drv_1"));
    }
}
