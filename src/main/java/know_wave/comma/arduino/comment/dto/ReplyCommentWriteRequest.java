package know_wave.comma.arduino.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class ReplyCommentWriteRequest {

    public static ReplyCommentWriteRequest create(String replyContent) {
        return new ReplyCommentWriteRequest(replyContent);
    }

    @JsonProperty("reply_content")
    private String replyContent;
}
