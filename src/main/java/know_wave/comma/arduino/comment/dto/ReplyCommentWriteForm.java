package know_wave.comma.arduino.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReplyCommentWriteForm {

    @JsonProperty("target_arduino_id")
    private Long arduinoId;

    @JsonProperty("target_comment_id")
    private Long commentId;

    @JsonProperty("reply_content")
    private String replyContent;
}
