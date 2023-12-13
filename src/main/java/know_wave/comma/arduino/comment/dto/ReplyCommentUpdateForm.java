package know_wave.comma.arduino.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReplyCommentUpdateForm {

    @JsonProperty("target_reply_comment_id")
    private Long replyCommentId;

    @JsonProperty("updated_reply_content")
    private String updatedReplyContent;
}
