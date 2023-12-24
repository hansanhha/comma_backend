package know_wave.comma.arduino.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import know_wave.comma.arduino.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplyCommentPageResponse {

    public static ReplyCommentPageResponse to(Page<Comment> comments) {
        return new ReplyCommentPageResponse(
                comments.stream().map(ReplyCommentResponse::to).toList(),
                comments.get().findFirst().get().getParent().getId(),
                comments.getSize(),
                comments.hasNext(),
                comments.isFirst(),
                comments.isLast());
    }

    private final List<ReplyCommentResponse> comments;
    @JsonProperty("parent_comment_id")
    private final Long parentCommentId;
    private final int size;
    private final boolean hasNext;
    private final boolean isFirst;
    private final boolean isLast;

    @Getter
    @RequiredArgsConstructor
    private static class ReplyCommentResponse {
        private final Long commentId;
        private final String accountId;
        private final String content;
        private final LocalDateTime createdAt;
        private final String contentStatus;
        private final String profileImage;

        private static ReplyCommentResponse to(Comment comment) {

            return new ReplyCommentResponse(
                    comment.getId(),
                    comment.getAccount().getId(),
                    comment.getContent(),
                    comment.getCreatedDate(),
                    comment.getContentStatus().getStatus(),
                    comment.getAccount().getProfileImage());
        }

    }
}
