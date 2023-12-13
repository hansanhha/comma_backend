package know_wave.comma.arduino.component.dto;

import know_wave.comma.arduino.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentPageResponse {

    public static CommentPageResponse of(Page<Comment> comments) {
        return new CommentPageResponse(
                comments.stream().map(CommentResponse::of).toList(),
                comments.getSize(),
                comments.hasNext(),
                comments.isFirst(),
                comments.isLast());
    }

    private final List<CommentResponse> comments;
    private final int size;
    private final boolean hasNext;
    private final boolean isFirst;
    private final boolean isLast;

    @Getter
    @RequiredArgsConstructor
    private static class CommentResponse {
        private final Long commentId;
        private final Long parentCommentId;
        private final String accountId;
        private final String content;
        private final LocalDateTime createdAt;
        private final String contentStatus;
        private final String profileImage;

        private static CommentResponse of(Comment comment) {
            return new CommentResponse(
                    comment.getId(),
                    comment.getParent().getId(),
                    comment.getAccount().getId(),
                    comment.getContent(),
                    comment.getCreatedDate(),
                    comment.getContentStatus().getStatus(),
                    comment.getAccount().getProfileImage());
        }

    }

}
