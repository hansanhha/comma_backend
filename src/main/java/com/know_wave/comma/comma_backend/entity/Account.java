package com.know_wave.comma.comma_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Persistable;

@Entity
public class Account extends BaseTimeEntity implements Persistable<String> {

    protected Account() {}

    public Account(String id, String email, String password, String academicNumber, AcademicMajor major, AcademicStatus academicStatus) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.academicNumber = academicNumber;
        this.major = major;
        this.academicStatus = academicStatus;
        this.role = Role.Member;
    }

    @Id
    @Column(name = "account_id")
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String academicNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicMajor major;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicStatus academicStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAcademicNumber() {
        return academicNumber;
    }

    public AcademicMajor getMajor() {
        return major;
    }

    public AcademicStatus getAcademicStatus() {
        return academicStatus;
    }

    public Role getRole() {
        return role;
    }
}
