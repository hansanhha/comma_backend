package know_wave.comma.arduino.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.entity.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArduinoPageResponse {

    public static ArduinoPageResponse to(Page<Arduino> slice) {
        return new ArduinoPageResponse(
                slice.stream().map(ArduinoResponse::to).toList(),
                slice.isFirst(),
                slice.isLast(),
                slice.hasNext(),
                slice.getSize());
    }

    @JsonProperty("arduinos")
    private final List<ArduinoResponse> arduinoList;

    @JsonProperty("is_first")
    private final Boolean isFirst;

    @JsonProperty("is_last")
    private final Boolean isLast;

    @JsonProperty("has_next")
    private final Boolean hasNext;

    @JsonProperty("size")
    private final int size;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ArduinoResponse {

        private final Long arduinoId;
        private final String name;
        private final int count;
        private final String stockStatus;
        private final List<String> categories;
        private final String thumbnail;

        private static ArduinoResponse to(Arduino arduino) {

            String thumbnail;

            if (arduino.getPhotos() == null || arduino.getPhotos().isEmpty()) {
                thumbnail = null;
            } else {
                thumbnail = arduino.getPhotos().getFirst().getFilePath();
            }

            return new ArduinoResponse(
                    arduino.getId(),
                    arduino.getName(),
                    arduino.getCount(),
                    arduino.getStockStatus().getStatus(),
                    arduino.getCategories().stream()
                            .map(Category::getName)
                            .toList(),
                    thumbnail);
        }

    }
}
