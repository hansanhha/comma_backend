package know_wave.comma.arduino.component.dto;

import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.entity.ArduinoCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArduinoPageResponse {

    public static ArduinoPageResponse of(Page<Arduino> slice) {
        return new ArduinoPageResponse(
                slice.stream().map(ArduinoResponse::of).toList(),
                slice.isFirst(),
                slice.isLast(),
                slice.hasNext(),
                slice.getSize());
    }

    private final List<ArduinoResponse> arduinoList;
    private final boolean isFirst;
    private final boolean isLast;
    private final boolean hasNext;
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

        private static ArduinoResponse of(Arduino arduino) {
            return new ArduinoResponse(arduino.getId(),
                    arduino.getName(),
                    arduino.getCount(),
                    arduino.getStockStatus().getStatus(),
                    arduino.getCategories().stream()
                            .map(ArduinoCategory::getName)
                            .toList(),
                    arduino.getPhotos().get(0).getFilePath());
        }

    }
}
