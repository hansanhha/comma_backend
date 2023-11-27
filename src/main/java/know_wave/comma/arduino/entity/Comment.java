package know_wave.comma.arduino.entity;

import know_wave.comma.account.entity.Account;
import know_wave.comma.common.entity.BaseTimeEntity;
import know_wave.comma.common.entity.ContentStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Comment extends BaseTimeEntity {

    protected Comment() {}

    public Comment(Arduino arduino, Account account, String content) {
        this.arduino = arduino;
        this.account = account;
        this.content = content;
        this.contentStatus = ContentStatus.CREATED;
        this.replies = new ArrayList<>();
    }

    public Comment(Arduino arduino, Account account, Comment parent, String content) {
        setParent(parent);
        this.arduino = arduino;
        this.account = account;
        this.content = content;
        this.contentStatus = ContentStatus.CREATED;
    }

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> replies = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private ContentStatus contentStatus;

    public boolean isNotWriter(String accountId) {
        return account.getId() != null && !account.getId().equals(accountId);
    }

    public boolean isDeleted() {
        return contentStatus == ContentStatus.DELETED;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
        parent.getReplies().add(this);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public ContentStatus getContentStatus() {
        return contentStatus;
    }

    public Comment getParent() {
        return parent;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public Account getAccount() {
        return account;
    }
}
