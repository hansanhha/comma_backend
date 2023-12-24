package know_wave.comma.config.security.entity;

import know_wave.comma.account.entity.Account;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

    public static Token create(Account account, String token, Date expiration) {
        return new Token(token, false, false, expiration, account);
    }

    private Token(String token, boolean revoked, boolean expired, Date expiration, Account account) {
        this.token = token;
        this.revoked = revoked;
        this.expired = expired;
        this.expiration = expiration;
        this.account = account;
    }

    @Id
    @GeneratedValue
    @Column(name = "token_id")
    private Long id;

    @Column(unique = true, updatable = false, nullable = false)
    private String token;

    @Setter
    private boolean revoked;

    @Setter
    private boolean expired;

    private Date expiration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

}