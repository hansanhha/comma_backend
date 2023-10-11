package com.know_wave.comma.comma_backend.account.dto;

import com.know_wave.comma.comma_backend.account.entity.AcademicMajor;
import com.know_wave.comma.comma_backend.account.entity.AcademicStatus;
import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.entity.auth.Role;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class AdminCreateForm {

    @NotEmpty(message = "{Required}")
    @Length(min = 4, max = 255, message = "{Length}")
    private String accountId;
    @NotEmpty(message = "{Required}")
    @Length(min = 8, max = 255, message = "{Length}")
    private String password;
    @NotEmpty(message = "{Required}")
    @Length(min = 2, max = 8, message = "{Length}")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
