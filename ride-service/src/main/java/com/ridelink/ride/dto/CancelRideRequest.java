package com.ridelink.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload for cancelling an active or pending ride")
public class CancelRideRequest {

    @Schema(description = "Detailed reason for cancellation", requiredMode = Schema.RequiredMode.REQUIRED, example = "Driver taking too long to arrive")
    @NotBlank(message = "cancellationReason is required")
    private String cancellationReason;

    @Schema(description = "Entity requesting cancellation (PASSENGER, DRIVER, SYSTEM)", example = "PASSENGER")
    private String cancelledBy;

    public CancelRideRequest() {}

    public CancelRideRequest(String cancellationReason, String cancelledBy) {
        this.cancellationReason = cancellationReason;
        this.cancelledBy = cancelledBy;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }
}
