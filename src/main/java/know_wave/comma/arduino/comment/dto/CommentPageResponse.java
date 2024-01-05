package know_wave.comma.arduino.comment.dto;

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

    public static CommentPageResponse to(Page<Comment> comments) {
        if (comments == null || comments.isEmpty())  {
            return null;
        }

        return new CommentPageResponse(
                comments.stream().map(CommentResponse::to).toList(),
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
    public static class CommentResponse {
        private final Long commentId;
        private final String accountId;
        private final String content;
        private final LocalDateTime createdAt;
        private final String contentStatus;
        private final String profileImage;

        private static CommentResponse to(Comment comment) {

            return new CommentResponse(
                    comment.getId(),
                    comment.getAccount().getId(),
                    comment.getContent(),
                    comment.getCreatedDate(),
                    comment.getContentStatus().getStatus(),
                    comment.getAccount().getProfileImage());
        }

    }

}
