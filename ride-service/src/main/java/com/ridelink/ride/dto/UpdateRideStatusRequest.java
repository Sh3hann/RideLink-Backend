package com.ridelink.ride.dto;

import com.ridelink.ride.model.RideStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request payload for updating ride status")
public class UpdateRideStatusRequest {

    @Schema(description = "Target ride status", requiredMode = Schema.RequiredMode.REQUIRED, example = "ACCEPTED")
    @NotNull(message = "status is required")
    private RideStatus status;

    @Schema(description = "Driver ID (if action performed by driver)", example = "drv_404")
    private String driverId;

    @Schema(description = "Optional reason when status is CANCELLED", example = "Driver could not reach pickup in time")
    private String cancellationReason;

    @Schema(description = "Actor initiating update (PASSENGER, DRIVER, ADMIN, SYSTEM)", example = "DRIVER")
    private String updatedBy;

    public UpdateRideStatusRequest() {}

    public UpdateRideStatusRequest(RideStatus status) {
        this.status = status;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
