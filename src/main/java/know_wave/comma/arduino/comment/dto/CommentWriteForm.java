package know_wave.comma.arduino.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentWriteForm {

    @JsonProperty("target_arduino_id")
    private final Long arduinoId;

    @JsonProperty("content")
    private final String content;

}
