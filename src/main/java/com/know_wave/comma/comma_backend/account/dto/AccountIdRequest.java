package com.know_wave.comma.comma_backend.account.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class AccountIdRequest {

    @NotEmpty(message = "아이디를 입력해주세요")
    @Length(min = 4, max = 255, message = "아이디는 최소 4글자 이상이어야 합니다")
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
