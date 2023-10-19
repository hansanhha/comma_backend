package com.know_wave.comma.comma_backend.order.entity;

public enum Subject {

    SENIER_PROJECT("졸업작품"),
    IOT_PROGRAMMING("IOT 프로그래밍");

    private final String subjectName;

    Subject(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
