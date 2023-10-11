package com.know_wave.comma.comma_backend.arduino.dto.order;

import com.know_wave.comma.comma_backend.arduino.entity.Subject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {

    @NotNull(message = "{Required}")
    private Subject subject;

    @NotEmpty(message = "{Required}")
    private String description;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
