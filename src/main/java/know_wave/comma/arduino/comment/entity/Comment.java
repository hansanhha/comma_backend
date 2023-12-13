package know_wave.comma.arduino.comment.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.common.entity.BaseTimeEntity;
import know_wave.comma.community.entity.ContentStatus;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "arduino_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Enumerated
    private ContentStatus contentStatus;

    public static Comment create(Arduino arduino, Account account, String content) {
        return new Comment(null, account, arduino, content, null, ContentStatus.CREATED);
    }

    public static Comment createReply(Arduino arduino, Account account, Comment parent, String content) {
        return new Comment(null, account, arduino, content, parent, ContentStatus.CREATED);
    }

    public void update(String content) {
        this.content = content;
    }

    public void delete() {
        this.contentStatus = ContentStatus.DELETED;
    }

    public boolean isNotWriter(Account account) {
        return !this.account.equals(account);
    }
}
