package com.ridelink.driver.dto;

import com.ridelink.driver.model.AvailabilityStatus;

public class UpdateAvailabilityRequest {
    private AvailabilityStatus status;

    public UpdateAvailabilityRequest() {
    }

    public AvailabilityStatus getStatus() {
        return status;
    }

    public void setStatus(AvailabilityStatus status) {
        this.status = status;
        
    }
}
