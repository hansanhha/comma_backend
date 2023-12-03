package know_wave.comma.arduino.entity.comment;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.arduino.entity.arduino.Arduino;
import know_wave.comma.community.entity.ContentCommunityStatus;

@Entity
public class Comment {

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
    private ContentCommunityStatus contentStatus;

}
