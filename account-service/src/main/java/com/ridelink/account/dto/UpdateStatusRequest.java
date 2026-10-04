package com.ridelink.account.dto;

import com.ridelink.account.model.AccountStatus;

public class UpdateStatusRequest {
    private AccountStatus status;

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
