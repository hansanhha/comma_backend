package com.know_wave.comma.comma_backend.account.entity.token;

import com.know_wave.comma.comma_backend.account.entity.Account;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Token {

    protected Token() {
    }

    public Token(String token, boolean revoked, boolean expired, Date expiration, Account account) {
        this.token = token;
        this.revoked = revoked;
        this.expired = expired;
        this.expiration = expiration;
        this.account = account;
    }

    @Id @GeneratedValue
    @Column(name = "token_id")
    private Long id;

    @Column(unique = true, updatable = false, nullable = false)
    private String token;

    private boolean revoked;
    private boolean expired;
    private Date expiration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Date getExpiration() {
        return expiration;
    }

    public Account getAccount() {
        return account;
    }

    public String getToken() {
        return token;
    }
}
