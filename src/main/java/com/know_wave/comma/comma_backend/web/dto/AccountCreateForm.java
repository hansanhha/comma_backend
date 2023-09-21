package com.know_wave.comma.comma_backend.web.dto;

import com.know_wave.comma.comma_backend.entity.AcademicMajor;
import com.know_wave.comma.comma_backend.entity.AcademicStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class AccountCreateForm {

    public AccountCreateForm() {
    }

    public AccountCreateForm(String id, String password, String email, String academicNumber, AcademicMajor major, AcademicStatus status) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.academicNumber = academicNumber;
        this.major = major;
        this.status = status;
    }

    @NotEmpty(message = "아이디를 입력해주세요")
    @Size(min = 4, max = 255, message = "아이디는 최소 4글자 이상이어야 합니다")
    private String id;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    @Size(min = 8, max = 255, message = "비밀번호는 최소 8자리 이상이어야 합니다")
    private String password;

    @NotEmpty(message = "이메일을 입력해주세요")
    @Email(regexp = "^[a-zA-Z0-9]{4,}+@m365\\.dongyang\\.ac\\.kr$",
            message = "학교 이메일만 사용할 수 있습니다")
    private String email;

    @NotEmpty(message = "학번을 입력해주세요")
    @Length(min = 8, max = 8, message = "올바르지 않은 학번입니다")
    @Range(min = 20000000, max = 20300000, message = "올바르지 않은 학번입니다")
    private String academicNumber;

    @NotNull(message = "전공을 입력해주세요")
    private AcademicMajor major;

    @NotNull(message = "학적 상태를 입력해주세요")
    private AcademicStatus status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public AcademicMajor getMajor() {
        return major;
    }

    public void setMajor(AcademicMajor major) {
        this.major = major;
    }

    public AcademicStatus getStatus() {
        return status;
    }

    public void setStatus(AcademicStatus status) {
        this.status = status;
    }
}
