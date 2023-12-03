package know_wave.comma.arduino_.entity;

import know_wave.comma.account.entity.Account;
import jakarta.persistence.*;

@Entity
@Table(name = "arduino_like")
public class Like {

    protected Like() {}

    public Like(Account account, Arduino arduino) {
        this.account = account;
        this.arduino = arduino;
    }

    @Id
    @GeneratedValue
    @Column(name = "arduino_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Like && ((Like) obj).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
