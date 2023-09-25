package com.know_wave.comma.comma_backend.account.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class AccountPasswordRequest {

    @NotEmpty(message = "비밀번호를 입력해주세요")
    @Length(min = 8, max = 255, message = "비밀번호는 최소 8자리 이상이어야 합니다")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
