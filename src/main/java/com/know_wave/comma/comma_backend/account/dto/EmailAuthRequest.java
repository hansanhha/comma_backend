package com.know_wave.comma.comma_backend.account.dto;

import com.know_wave.comma.comma_backend.util.StringStorage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import static com.know_wave.comma.comma_backend.util.StringStorage.emailRegex;

public class EmailAuthRequest {

    @NotEmpty(message = "{Required}")
    @Email(regexp = emailRegex, message = "{Email}")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
