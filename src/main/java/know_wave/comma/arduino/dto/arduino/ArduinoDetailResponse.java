package know_wave.comma.arduino.dto.arduino;

import know_wave.comma.arduino.entity.Arduino;
import know_wave.comma.arduino.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public class ArduinoDetailResponse {

    public ArduinoDetailResponse(Long arduinoId, String arduinoName, int remainingCount, String arduinoDescription, List<String> arduinoCategories, List<CommentResponse> arduinoCommentResponses) {
        this.arduinoId = arduinoId;
        this.arduinoName = arduinoName;
        this.remainingCount = remainingCount;
        this.arduinoDescription = arduinoDescription;
        this.arduinoCategories = arduinoCategories;
        this.arduinoComments = arduinoCommentResponses;
    }

    public static ArduinoDetailResponse of(Arduino arduino, List<Comment> comments) {
        return new ArduinoDetailResponse(
                arduino.getId(),
                arduino.getName(),
                arduino.getCount(),
                arduino.getDescription(),
                arduino.getCategories(),
                makeComments(comments)
        );
    }

    private static List<CommentResponse> makeComments(List<Comment> comments) {
        return comments.stream()
                .map(CommentResponse::of)
                .toList();
    }

    private final Long arduinoId;

    private final String arduinoName;

    private final int remainingCount;

    private final String arduinoDescription;

    private final List<String> arduinoCategories;

    private final List<CommentResponse> arduinoComments;

    public Long getArduinoId() {
        return arduinoId;
    }

    public String getArduinoName() {
        return arduinoName;
    }

    public int getRemainingCount() {
        return remainingCount;
    }

    public String getArduinoDescription() {
        return arduinoDescription;
    }

    public List<String> getArduinoCategories() {
        return arduinoCategories;
    }

    public List<CommentResponse> getArduinoComments() {
        return arduinoComments;
    }

    public static class CommentResponse {
        private final Long commentId;
        private final String commentContent;
        private final String commentWriter;
        private final LocalDateTime commentCreatedAt;
        private final LocalDateTime commentUpdatedAt;
        private final boolean deleted;

        public CommentResponse(Long commentId, String commentContent, String commentWriter, LocalDateTime commentCreatedAt, LocalDateTime commentUpdatedAt, boolean deleted) {
            this.commentId = commentId;
            this.commentWriter = commentWriter;
            this.commentCreatedAt = commentCreatedAt;
            this.commentUpdatedAt = commentUpdatedAt;
            this.deleted = deleted;
            if (deleted) {
                this.commentContent = "삭제된 댓글입니다.";
                return;
            }
            this.commentContent = commentContent;
        }

        public static CommentResponse of(Comment comment) {
            return new CommentResponse(
                    comment.getId(),
                    comment.getContent(),
                    comment.getAccount().getId(),
                    comment.getCreatedDate(),
                    comment.getLastModifiedDate(),
                    comment.isDeleted()
            );
        }

        public Long getCommentId() {
            return commentId;
        }

        public String getCommentContent() {
            return commentContent;
        }

        public String getCommentWriter() {
            return commentWriter;
        }

        public LocalDateTime getCommentCreatedAt() {
            return commentCreatedAt;
        }

        public LocalDateTime getCommentUpdatedAt() {
            return commentUpdatedAt;
        }

        public boolean isDeleted() {
            return deleted;
        }
    }

}


