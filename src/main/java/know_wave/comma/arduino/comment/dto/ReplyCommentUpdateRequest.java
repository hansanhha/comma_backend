package know_wave.comma.arduino.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ReplyCommentUpdateRequest {

    @JsonProperty("updated_reply_content")
    private String updatedReplyContent;
}
