package com.know_wave.comma.comma_backend.entity;

import com.know_wave.comma.comma_backend.util.EmailSender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class AccountVerify extends BaseTimeEntity implements Persistable {

    protected AccountVerify() {
    }

    public AccountVerify(String email, Boolean verified, int code) {
        this.email = email;
        this.verified = verified;
        this.code = code;
    }

    @Id
    @Column(name = "auth_email", updatable = false)
    private String email;

    private Boolean verified;

    @Column(nullable = false)
    private int code;

    public Object getId() {
        return email;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    private static final String subject = "COMMA 메일 요청";

    public void sendCode(EmailSender emailSender) {
        emailSender.send(email, subject, String.valueOf(code));
    }

    public boolean verifyCode(int code) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastModifiedDate = getLastModifiedDate();
        long expiration = ChronoUnit.MINUTES.between(lastModifiedDate, now);

        if (expiration > 5) {
            return false;
        }

        return this.code == code;
    }

    public boolean isVerified() {
        return verified;
    }
}
