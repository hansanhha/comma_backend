package know_wave.comma.arduino.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import know_wave.comma.arduino.comment.dto.CommentPageResponse;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.comment.entity.Comment;
import know_wave.comma.arduino.component.entity.ArduinoPhoto;
import know_wave.comma.arduino.component.entity.Category;
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

    public static ArduinoDetailResponse to(Arduino arduino, Page<Comment> comments) {
        return new ArduinoDetailResponse(
                arduino.getId(),
                arduino.getName(),
                arduino.getCount(),
                arduino.getDescription(),
                arduino.getStockStatus().getStatus(),
                arduino.getCategories().stream()
                        .map(Category::getName)
                        .toList(),
                ArduinoPhotoResponse.to(arduino.getPhotos()),
                CommentPageResponse.to(comments));
    }

    public static ArduinoDetailResponse to(Arduino arduino) {
        return new ArduinoDetailResponse(
                arduino.getId(),
                arduino.getName(),
                arduino.getCount(),
                arduino.getDescription(),
                arduino.getStockStatus().getStatus(),
                arduino.getCategories().stream()
                        .map(Category::getName)
                        .toList(),
                ArduinoPhotoResponse.to(arduino.getPhotos()),
                null);
    }

    @JsonProperty("arduino_id")
    private final Long arduinoId;
    @JsonProperty("name")
    private final String name;
    @JsonProperty("count")
    private final int count;
    @JsonProperty("description")
    private final String description;
    @JsonProperty("stock_status")
    private final String stockStatus;
    @JsonProperty("categories")
    private final List<String> categories;
    @JsonProperty("photos")
    private final List<ArduinoPhotoResponse> photos;
    @JsonProperty("comments")
    private final CommentPageResponse comments;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ArduinoPhotoResponse {

        private static List<ArduinoPhotoResponse> to(List<ArduinoPhoto> photos){
            return photos.stream()
                    .map(photo -> new ArduinoPhotoResponse(photo.getFileUuid(), photo.getFileName(), photo.getFilePath(), photo.getSize(), photo.getArduino().getId()))
                    .toList();
        }

        private final String fileUuid;
        private final String fileName;
        private final String filePath;
        private final Long fileSize;
        private final Long arduinoId;

    }
}
