package com.know_wave.comma.comma_backend.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class EmailAuthRequestDto {

    @NotEmpty(message = "이메일을 입력해주세요")
    @Email(regexp = "^[a-zA-Z0-9]{4,}+@m365\\.dongyang\\.ac\\.kr$",
            message = "학교 이메일만 사용할 수 있습니다")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
