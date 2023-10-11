package com.know_wave.comma.comma_backend.account.entity;

public enum AcademicMajor {
    SoftwareEngineering("컴퓨터소프트웨어공학과"),
    informationEngineering("컴퓨터정보공학과"),
    AIEngineering("인공지능소프트웨어학과");

    private String major;

    AcademicMajor(String major) {
        this.major = major;
    }

    public String getMajor() {
        return major;
    }
}
