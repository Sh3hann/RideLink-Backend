package com.ridelink.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload for explicitly assigning a driver to a ride")
public class AssignDriverRequest {

    @Schema(description = "Driver identifier to assign", requiredMode = Schema.RequiredMode.REQUIRED, example = "drv_404")
    @NotBlank(message = "driverId is required")
    private String driverId;

    public AssignDriverRequest() {}

    public AssignDriverRequest(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
}
