package know_wave.comma.arduino.dto.arduino;

import know_wave.comma.arduino.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public class ArduinoReplyCommentResponse {


    public static List<ArduinoReplyCommentResponse> of(List<Comment> comments) {
        return comments.stream()
                .map(ArduinoReplyCommentResponse::of)
                .toList();
    }

    private static ArduinoReplyCommentResponse of(Comment comment) {
        return new ArduinoReplyCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAccount().getId(),
                comment.getCreatedDate(),
                comment.getLastModifiedDate(),
                comment.isDeleted(),
                comment.getParent().getId()
        );
    }

    public ArduinoReplyCommentResponse(Long replyCommentId, String replyCommentContent, String replyCommentWriter, LocalDateTime replyCommentCreatedAt, LocalDateTime replyCommentUpdatedAt, boolean replyDeleted, Long replyCommentParentId) {
        this.replyCommentId = replyCommentId;
        this.replyCommentWriter = replyCommentWriter;
        this.replyCommentCreatedAt = replyCommentCreatedAt;
        this.replyCommentUpdatedAt = replyCommentUpdatedAt;
        this.replyCommentParentId = replyCommentParentId;
        this.replyDeleted = replyDeleted;
        if (replyDeleted) {
            this.replyCommentContent = "삭제된 댓글입니다.";
            return;
        }
        this.replyCommentContent = replyCommentContent;
    }

    private final Long replyCommentId;
    private final String replyCommentContent;
    private final String replyCommentWriter;
    private final LocalDateTime replyCommentCreatedAt;
    private final LocalDateTime replyCommentUpdatedAt;
    private final boolean replyDeleted;
    private final Long replyCommentParentId;

    public Long getReplyCommentId() {
        return replyCommentId;
    }

    public String getReplyCommentContent() {
        return replyCommentContent;
    }

    public String getReplyCommentWriter() {
        return replyCommentWriter;
    }

    public LocalDateTime getReplyCommentCreatedAt() {
        return replyCommentCreatedAt;
    }

    public LocalDateTime getReplyCommentUpdatedAt() {
        return replyCommentUpdatedAt;
    }

    public boolean isReplyDeleted() {
        return replyDeleted;
    }

    public Long getReplyCommentParentId() {
        return replyCommentParentId;
    }
}
