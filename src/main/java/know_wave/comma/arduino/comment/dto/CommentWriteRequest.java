package know_wave.comma.arduino.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CommentWriteRequest {

    @NotEmpty(message = "댓글 내용을 입력해주세요.")
    @JsonProperty("content")
    private String content;

}
