package know_wave.comma.arduino.component.dto;

import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;


// TODO : 아두이노 좋아요 카운트 추가
// TODO : 아두이노 댓글 좋아요 카운트 추가
@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArduinoDetailResponse {

    public static ArduinoDetailResponse of(Arduino arduino, Page<Comment> comments) {
        return new ArduinoDetailResponse(
                arduino.getId(),
                arduino.getName(),
                arduino.getCount(),
                arduino.getDescription(),
                arduino.getStockStatus().getStatus(),
                arduino.getCategories().stream()
                        .map(category -> category.getName())
                        .toList(),
                ArduinoPhotosResponse.of(arduino.getPhotos()),
                CommentPageResponse.of(comments));
    }

    public static ArduinoDetailResponse of(Arduino arduino) {
        return new ArduinoDetailResponse(
                arduino.getId(),
                arduino.getName(),
                arduino.getCount(),
                arduino.getDescription(),
                arduino.getStockStatus().getStatus(),
                arduino.getCategories().stream()
                        .map(category -> category.getName())
                        .toList(),
                ArduinoPhotosResponse.of(arduino.getPhotos()),
                null);
    }

    private final Long arduinoId;
    private final String name;
    private final int count;
    private final String description;
    private final String stockStatus;
    private final List<String> categories;
    private final ArduinoPhotosResponse photos;
    private final CommentPageResponse comments;
}
