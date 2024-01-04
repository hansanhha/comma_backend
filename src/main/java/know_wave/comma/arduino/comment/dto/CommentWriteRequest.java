package know_wave.comma.arduino.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class CommentWriteRequest {

    public static CommentWriteRequest create(String content) {
        return new CommentWriteRequest(content);
    }

    @NotEmpty(message = "댓글 내용을 입력해주세요.")
    @JsonProperty("content")
    private String content;

}
