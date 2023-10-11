package com.know_wave.comma.comma_backend.account.dto;

import com.know_wave.comma.comma_backend.account.entity.Account;

public class AccountResponse {

    public static AccountResponse of(Account account) {
        return new AccountResponse(
                account.getName(),
                account.getEmail(),
                account.getAcademicNumber(),
                account.getAcademicStatus(),
                account.getAcademicMajor(),
                account.getRole().getGrade()
        );
    }
    private AccountResponse(String name, String email, String academicNumber, String academicStatus, String academicMajor, String permission) {
        this.name = name;
        this.email = email;
        this.academicNumber = academicNumber;
        this.academicStatus = academicStatus;
        this.academicMajor = academicMajor;
        this.permission = permission;
    }

    private String name;
    private String email;
    private String academicNumber;
    private String academicStatus;

    private String academicMajor;

    private String permission;

    public AccountResponse() {
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAcademicNumber() {
        return academicNumber;
    }

    public void setAcademicNumber(String academicNumber) {
        this.academicNumber = academicNumber;
    }

    public String getAcademicMajor() {
        return academicMajor;
    }

    public void setAcademicMajor(String academicMajor) {
        this.academicMajor = academicMajor;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
