package com.know_wave.comma.comma_backend.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public class EmailVerifyRequest {

    @NotEmpty(message = "이메일을 입력해주세요")
    @Email(regexp = "^[a-zA-Z0-9]{4,}+@m365\\.dongyang\\.ac\\.kr$",
            message = "학교 이메일만 사용할 수 있습니다")
    private String email;

    @NotNull(message = "인증번호를 입력해주세요")
    @Range(min = 111111, max = 999999, message = "올바르지 않은 인증번호입니다")
    private int code;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
