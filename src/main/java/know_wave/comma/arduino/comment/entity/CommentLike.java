package know_wave.comma.arduino.comment.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static CommentLike create(Account account, Comment comment) {
        return new CommentLike(null, comment, account);
    }
}
