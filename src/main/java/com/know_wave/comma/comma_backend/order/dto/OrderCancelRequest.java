package com.know_wave.comma.comma_backend.order.dto;

import jakarta.validation.constraints.NotEmpty;

public class OrderCancelRequest {

    @NotEmpty(message = "{Required}")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
