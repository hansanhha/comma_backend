package know_wave.comma.account.entity;

import know_wave.comma.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVerify extends BaseTimeEntity implements Persistable {

    public static EmailVerify create(String email, int code) {
        return new EmailVerify(email, false, code);
    }

    @Id
    @Column(name = "account_verify_email", updatable = false)
    private String email;

    @Setter
    private Boolean verified;

    @Setter
    @Column(nullable = false)
    private int code;

    public Object getId() {
        return email;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }

    public boolean isValidCode(int code) {
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
