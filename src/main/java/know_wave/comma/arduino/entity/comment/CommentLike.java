package know_wave.comma.arduino.entity.comment;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;

@Entity
public class CommentLike {

    @Id
    @GeneratedValue
    @Column(name = "comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
