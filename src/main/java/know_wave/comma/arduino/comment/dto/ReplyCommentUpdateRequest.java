package know_wave.comma.arduino.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class ReplyCommentUpdateRequest {

    public static ReplyCommentUpdateRequest create(String updatedReplyContent) {
        return new ReplyCommentUpdateRequest(updatedReplyContent);
    }

    @JsonProperty("update_reply_content")
    private String updatedReplyContent;
}
